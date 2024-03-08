package hello.advanced.logtrace;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FieldLogTrace implements LogTrace{

	private static final String START_PREFIX = "-->";
	private static final String COMPLETE_PREFIX = "<--";
	private static final String EX_PREFIX = "<X--";

	private TraceId traceIdHolder; // TraceID를 동기화하는 역할을 담당하게 됨. (동시성 이슈 발생)

	@Override
	public TraceStatus begin(String message) {
		syncTraceId();
		TraceId traceId = traceIdHolder;
		Long startTimeMs = System.currentTimeMillis();
		log.info("[" + traceId.getId() +"]" + addSpace(START_PREFIX, traceId.getLevel()) + message);
		return new TraceStatus(traceId, startTimeMs, message);
	}

	@Override
	public void end(TraceStatus status) {
		complete(status, null);
	}

	@Override
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

		releaseTraceId();
	}

	private static String addSpace(String prefix, int level){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < level; i++) {
			sb.append( (i == level - 1) ? "|" + prefix : "|   ");
		}
		return sb.toString();
	}

	private void syncTraceId(){
		if(traceIdHolder == null){
			traceIdHolder = new TraceId();
		} else{
			traceIdHolder = traceIdHolder.createNextId();
		}
	}

	private void releaseTraceId(){
		if(traceIdHolder.isFirstLevel()){
			traceIdHolder = null;
		} else {
			traceIdHolder = traceIdHolder.createPreviousId();
		}
	}
}
