package com.example.jdbc.exception.basic;

import java.net.ConnectException;
import java.sql.SQLException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class CheckedAppTest {

	@Test
	void checked(){
		Controller controller = new Controller();
		Assertions.assertThatThrownBy(() -> controller.request()).isInstanceOf(Exception.class);
	}

	static class Controller{
		Service service = new Service();

		public void request() throws SQLException, ConnectException {
			service.logic();
		}


	}

	static class Service {
		Repository repository = new Repository();
		NetworkClient networkClient = new NetworkClient();

		public void logic() throws ConnectException, SQLException { // 모든 체크예외 다 던져짐.{
			repository.call();
			networkClient.call();
		}

	}

	static class NetworkClient{
		public void call() throws ConnectException{
			throw new ConnectException("연결 실패");
		}

	}

	static class Repository{
		public void call() throws SQLException{
			throw new SQLException();
		}
	}
}
