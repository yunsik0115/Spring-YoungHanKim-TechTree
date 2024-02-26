package com.example.jdbc.exception.basic;

import java.net.ConnectException;
import java.sql.SQLException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class UncheckedAppTest {

	@Test
	void unChecked(){
		Controller controller = new Controller();
		Assertions.assertThatThrownBy(controller::request).isInstanceOf(Exception.class);
	}

	static class Controller {
		Service service = new Service();

		public void request(){
			service.logic();
		}
	}

	static class Service {

		Repository repository = new Repository();
		NetworkClient networkClient = new NetworkClient();

		public void logic() {
			repository.call();
			networkClient.call();
		}

	}

	static class NetworkClient {
		public void call() {
			throw new RuntimeConnectException("연결 실패");
		}
	}

	static class Repository {
		public void call(){
			try{
				runSQL();
			} catch (SQLException e){
				throw new RuntimeSQLException(e);
			}
		}

		public void runSQL() throws SQLException{
			throw new SQLException("ex");
		}
	}

	static class RuntimeConnectException extends RuntimeException {
		public RuntimeConnectException(String message){
			super(message);
		}
	}

	static class RuntimeSQLException extends RuntimeException {
		public RuntimeSQLException(){

		}

		public RuntimeSQLException(Throwable cause){ // cause(이전 예외를 담아서 넘길 수 있음)
			super(cause);
		}
	}
}
