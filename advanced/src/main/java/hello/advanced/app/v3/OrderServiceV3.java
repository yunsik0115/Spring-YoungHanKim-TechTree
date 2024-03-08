package hello.advanced.app.v3;

import org.springframework.stereotype.Repository;

import hello.advanced.app.v2.OrderRepositoryV2;
import hello.advanced.logtrace.LogTrace;
import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderServiceV3 {
	private final OrderRepositoryV2 orderRepository;
	private final LogTrace trace;

	public void orderItem(TraceId traceId, String itemId){
		TraceStatus status = null;

		try{
			status = trace.begin("OrderService.orderItem()");
			orderRepository.save(status.getTraceId(), itemId);
			trace.end(status);
		} catch (Exception e) {
			trace.exception(status, e);
			throw e;
		}
	}
}
