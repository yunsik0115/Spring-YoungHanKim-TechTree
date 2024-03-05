package hello.advanced.trace;

public class TraceStatus {
	private TraceId traceId;
	private Long startTimeMs; // (로그 시작할 때 상태 기록) -> 종료할 때 실제 걸린 시간 계산 가능
	private String message;

	public TraceStatus(TraceId traceId, Long startTimeMs, String message) {
		this.traceId = traceId;
		this.startTimeMs = startTimeMs;
		this.message = message;
	}

	public TraceId getTraceId() {
		return traceId;
	}

	public Long getStartTimeMs() {
		return startTimeMs;
	}

	public String getMessage() {
		return message;
	}
}
