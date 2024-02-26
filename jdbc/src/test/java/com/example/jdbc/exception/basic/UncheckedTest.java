package com.example.jdbc.exception.basic;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UncheckedTest {

	@Test
	void uncatchedException() {
		Service service = new Service();
		service.callCatch();
	}

	@Test
	void uncheckedThrow() {
		Service service = new Service();
		//service.callThrow(); 실패하는게 정상
		Assertions.assertThatThrownBy(service::callThrow).isInstanceOf(MyUncheckedException.class);
	}

	static class MyUncheckedException extends RuntimeException {
		public MyUncheckedException(String message){
			super(message);
		}
	}

	static class Repository {
		public void call() {
			throw new MyUncheckedException("ex");
		}
	}

	static class Service {
		Repository repository = new Repository();

		public void callCatch() {
			try {
				repository.call(); // 예외를 잡아도 되고 안잡아도 됨.
			} catch (MyUncheckedException e){
				log.info("예외처리 message = {}", e.getMessage(), e);
			}
		}

		public void callThrow(){
			repository.call(); // 자연스럽게 상위로 넘어간다.
		}
	}
}
