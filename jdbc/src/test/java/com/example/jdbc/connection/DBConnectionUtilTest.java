package com.example.jdbc.connection;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.sql.Connection;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j // 주의 메서드 선택시 메서드 내부에서 실행되는 로그만 찍힌다.
class DBConnectionUtilTest {
	@Test
	void connection(){
		Connection connection = DBConnectionUtil.getConnection();
		assertThat(connection).isNotNull();
	}
}
