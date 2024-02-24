package com.example.jdbc.service;

import static com.example.jdbc.connection.ConnectionConst.*;

import java.sql.SQLException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.example.jdbc.domain.Member;
import com.example.repository.MemberRepositoryV1;

/**
 * 기본 동작으로 트랜잭션이 없어서 문제 발생
 */
class MemberServiceV1Test {

	public static final String MEMBER_A = "memberA";
	public static final String MEMBER_B = "memberB";
	public static final String MEMBER_EX = "ex";

	private MemberRepositoryV1 memberRepository;

	private MemberServiceV1 memberService;

	@BeforeEach
	void before(){
		DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
		memberRepository = new MemberRepositoryV1(dataSource);
		memberService = new MemberServiceV1(memberRepository);
	}

	@AfterEach
	void after() throws SQLException{
		// 리소스 정리
		memberRepository.delete(MEMBER_A);
		memberRepository.delete(MEMBER_B);
	}

	@Test
	@DisplayName("정상 이체")
	void accountTransfer() throws SQLException {
		// Given
		Member memberA = new Member(MEMBER_A, 10000);
		Member memberB = new Member(MEMBER_B, 10000);
		memberRepository.save(memberA);
		memberRepository.save(memberB);

		// When
		memberService.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);

		// Then
		Member findMemberA = memberRepository.findById(memberA.getMemberId());
		Member findMemberB = memberRepository.findById(memberB.getMemberId());

		Assertions.assertThat(findMemberA.getMoney()).isEqualTo(8000);
		Assertions.assertThat(findMemberB.getMoney()).isEqualTo(12000);
	}

}