package hello.advanced.app.v3;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hello.advanced.app.OrderServiceV2;
import hello.advanced.logtrace.LogTrace;
import hello.advanced.trace.TraceStatus;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class OrderControllerV3 {

	private final OrderServiceV2 orderService;
	private final LogTrace trace;

	@GetMapping("/v3/request")
	public String request(@RequestParam(value = "itemId") String itemId){
		TraceStatus status = null;
		try {
			status =  trace.begin("OrderController.request()");
			orderService.orderItem(status.getTraceId(), itemId);
			trace.end(status);
			return "ok";
		} catch (Exception e){
			trace.exception(status, e);
			throw e;
		}
	}


}
