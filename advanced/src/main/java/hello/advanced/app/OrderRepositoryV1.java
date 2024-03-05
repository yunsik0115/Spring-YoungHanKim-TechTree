package hello.advanced.app;

import org.springframework.stereotype.Repository;

import hello.advanced.trace.HelloTraceV1;
import hello.advanced.trace.TraceStatus;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV1 {

	private final HelloTraceV1 trace;

	public void save(String itemId){
		TraceStatus status = null;

		try {
			status = trace.begin("OrderRepository.save()");

			if (itemId.equals("ex")) {
				throw new IllegalStateException("예외 발생!");
			}
			sleep(1000);
			trace.end(status);
		} catch (Exception e){
			trace.exception(status, e);
			throw e;
		}
	}

	private void sleep(int millis){
		try {
			Thread.sleep(millis);
		} catch(InterruptedException e){
			e.printStackTrace();
		}
	}

}
