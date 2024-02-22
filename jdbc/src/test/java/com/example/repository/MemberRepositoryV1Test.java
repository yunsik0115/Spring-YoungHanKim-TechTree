package com.example.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.example.jdbc.connection.ConnectionConst;
import com.example.jdbc.domain.Member;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class MemberRepositoryV1Test {

	MemberRepositoryV1 repository;

	@BeforeEach
	void beforeEach(){
		//DriverManagerDataSource dataSource = new DriverManagerDataSource(ConnectionConst.URL, ConnectionConst.USERNAME,
			//ConnectionConst.PASSWORD);
		//repository = new MemberRepositoryV1(dataSource);

		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setJdbcUrl(ConnectionConst.URL);
		dataSource.setPassword(ConnectionConst.PASSWORD);
		dataSource.setUsername(ConnectionConst.USERNAME);

		repository = new MemberRepositoryV1(dataSource);
	}

	@Test
	void crud() throws SQLException {
		Member member = new Member("memberV122", 10000);
		repository.save(member);

		Member findMember = repository.findById(member.getMemberId());
		log.info("findMember = {}", findMember);

		Assertions.assertThat(findMember).isEqualTo(member);

		//update : money 10000 -> 20000
		repository.update(member.getMemberId(), 20000);
		Member updatedMember = repository.findById(member.getMemberId());
		Assertions.assertThat(updatedMember.getMoney()).isEqualTo(20000);

		repository.delete(member.getMemberId());
		Assertions.assertThatThrownBy(() -> repository.findById(member.getMemberId())).isInstanceOf(
			NoSuchElementException.class);
	}
}