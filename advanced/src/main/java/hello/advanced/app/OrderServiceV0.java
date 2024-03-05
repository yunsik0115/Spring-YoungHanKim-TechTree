package hello.advanced.app;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceV0 {

	private final OrderRepositoryV0 orderRepository;

	public void orderItem(String itemId){
		orderRepository.save(itemId);
	}

}
