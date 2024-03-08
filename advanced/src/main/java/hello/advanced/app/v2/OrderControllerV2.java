package hello.advanced.app.v2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hello.advanced.trace.HelloTraceV2;
import hello.advanced.trace.TraceStatus;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OrderControllerV2 {

	private final OrderServiceV2 orderService;
	private final HelloTraceV2 trace;

	@GetMapping("/v2/request")
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
