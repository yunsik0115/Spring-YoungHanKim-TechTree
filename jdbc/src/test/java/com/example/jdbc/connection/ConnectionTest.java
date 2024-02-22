package com.example.jdbc.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

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

	private void useDataSource(DataSource dataSource) throws SQLException{
		Connection con1 = dataSource.getConnection();
		Connection con2 = dataSource.getConnection();

		log.info("connection = {}, class = {}", con1, con1.getClass());
		log.info("connection = {}, class = {}", con2, con2.getClass());
	}

}
