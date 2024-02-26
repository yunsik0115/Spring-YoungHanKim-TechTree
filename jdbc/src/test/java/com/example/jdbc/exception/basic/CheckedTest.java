package com.example.jdbc.exception.basic;

import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckedTest {

	@Test
	void checked_catch(){
		Service service = new Service();
		service.callCatch();
	}

	/**
	 * Exception을 상속받은 예외는 체크 예외가 된다.
	 */

	static class MyCheckedException extends Exception {
		public MyCheckedException(String message){
			super(message);
		}
	}

	static class Service{
		Repository repository = new Repository();

		/**
		 * 예외를 잡아서 처리하는 코드
		 */

		public void callCatch(){
			try {
				repository.call(); // 예외를 잡거나 던지거나
			} catch (MyCheckedException e) {
				log.info("예외 처리, 메세지 = {}", e.getMessage(), e);
			}
		}
	}

	static class Repository{
		public void call() throws MyCheckedException {
			throw new MyCheckedException("ex"); // throws로 던지거나 try ~ catch로 처리하거나
		}
	}
}
