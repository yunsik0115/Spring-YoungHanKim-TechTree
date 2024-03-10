package hello.proxy.app.v3;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrderControllerV3 {
	private final OrderServiceV3 orderService;

	public OrderControllerV3(OrderServiceV3 orderService) {
		this.orderService = orderService;
	}

	@GetMapping("/v3/request")
	public String request(@RequestParam(value = "itemId") String itemId){
		return "ok";
	}

	@GetMapping("/v3/no-log")
	public String request(){
		return "ok";
	}
}
