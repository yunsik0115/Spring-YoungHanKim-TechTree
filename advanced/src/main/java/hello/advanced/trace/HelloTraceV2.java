package hello.advanced.trace;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HelloTraceV2 {
	private static final String START_PREFIX = "-->";
	private static final String COMPLETE_PREFIX = "<--";
	private static final String EX_PREFIX = "<X--";

	public TraceStatus begin(String message) {
		TraceId traceId = new TraceId();
		Long startTimeMs = System.currentTimeMillis();
		log.info("[" + traceId.getId() +"]" + addSpace(START_PREFIX, traceId.getLevel()) + message);
		return new TraceStatus(traceId, startTimeMs, message);
	}

	public TraceStatus beginSync(TraceId beforeTraceId, String message) {
		TraceId nextId = beforeTraceId.createNextId();
		Long startTimeMs = System.currentTimeMillis();
		log.info("[" + nextId.getId() +"]" + addSpace(START_PREFIX, nextId.getLevel()) + message);
		return new TraceStatus(nextId, startTimeMs, message);
	}

	public void end(TraceStatus status) {
		complete(status, null);
	}

	public void exception(TraceStatus status, Exception e){
		complete(status, e);
	}

	public void complete(TraceStatus status, Exception e){
		Long stopTimeMs = System.currentTimeMillis();
		long resultTimeMs = stopTimeMs - status.getStartTimeMs();
		TraceId traceId = status.getTraceId();

		if (e == null) {
			log.info("[" + traceId.getId() + "]" + addSpace(COMPLETE_PREFIX, traceId.getLevel()) + "message = " + status.getMessage() + " time = " + resultTimeMs);
		} else {
			log.info("[" + traceId.getId() + "]" + addSpace(COMPLETE_PREFIX, traceId.getLevel()) + "message = " + status.getMessage() + " time = " + resultTimeMs + ", ex = " + e.getMessage());
		}
	}

	private static String addSpace(String prefix, int level){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < level; i++) {
			sb.append( (i == level - 1) ? "|" + prefix : "|   ");
		}
		return sb.toString();
	}
}
