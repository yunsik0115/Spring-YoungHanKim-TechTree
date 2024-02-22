package com.example.jdbc.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConnectionTest {

	@Test
	void driverManager() throws SQLException {
		Connection connection1 = DriverManager.getConnection(ConnectionConst.URL, ConnectionConst.USERNAME,
			ConnectionConst.PASSWORD);
		Connection connection2 = DriverManager.getConnection(ConnectionConst.URL, ConnectionConst.USERNAME,
			ConnectionConst.PASSWORD);

		log.info("connection = {}, class = {}", connection1, connection1.getClass());
		log.info("connection = {}, class = {}", connection2, connection2.getClass());
	}

	@Test
	void dataSourceDriverManager() throws SQLException{
		// DriverManagerDataSource의 경우 항상 새로운 커넥션 획득
		DriverManagerDataSource dataSource = new DriverManagerDataSource(ConnectionConst.URL,
			ConnectionConst.USERNAME,
			ConnectionConst.PASSWORD);
		useDataSource(dataSource);
	}

	@Test
	void dataSourceConnectionPool() throws SQLException, InterruptedException {
		// Connection Pooling with HikariCP
		HikariDataSource hikariDataSource = new HikariDataSource();
		hikariDataSource.setJdbcUrl(ConnectionConst.URL);
		hikariDataSource.setUsername(ConnectionConst.USERNAME);
		hikariDataSource.setPassword(ConnectionConst.PASSWORD);
		hikariDataSource.setMaximumPoolSize(10);
		hikariDataSource.setPoolName("MyPool");

		useDataSource(hikariDataSource);
		Thread.sleep(1000);
		// 데이터 풀 생성은 별도의 스레드에서 진행하기 때문에 테스트 스레드가 먼저 종료되는 경우 해당 스레드 로그 확인 불가.
	}

	private void useDataSource(DataSource dataSource) throws SQLException{
		Connection con1 = dataSource.getConnection();
		Connection con2 = dataSource.getConnection();

		log.info("connection = {}, class = {}", con1, con1.getClass());
		log.info("connection = {}, class = {}", con2, con2.getClass());
	}

}
