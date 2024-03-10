package hello.proxy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.proxy.app.v1.OrderControllerV1;
import hello.proxy.app.v1.OrderControllerV1Impl;
import hello.proxy.app.v1.OrderRepositoryV1;
import hello.proxy.app.v1.OrderRepositoryV1Impl;
import hello.proxy.app.v1.OrderServiceImplV1;
import hello.proxy.app.v1.OrderServiceV1;

@Configuration
public class AppV1Config {

	@Bean
	public OrderControllerV1 orderController(){
		return new OrderControllerV1Impl(orderService());
	}

	@Bean
	public OrderServiceV1 orderService(){
		return new OrderServiceImplV1(orderRepository());
	}

	@Bean
	public OrderRepositoryV1 orderRepository(){
		return new OrderRepositoryV1Impl();
	}
}
