package com.example.jdbc.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DBConnectionUtil {
	public static Connection getConnection(){
		try {
			// JDBC에 연결하기 위해 DriverManager로부터 Connection을 가져온다.
			Connection connection = DriverManager.getConnection(ConnectionConst.URL, ConnectionConst.USERNAME,
				ConnectionConst.PASSWORD);
			log.info("get connection = {}, class ={}", connection, connection.getClass());
			return connection;
		} catch(SQLException e){
			throw new IllegalStateException(e);
		}
	}
}
