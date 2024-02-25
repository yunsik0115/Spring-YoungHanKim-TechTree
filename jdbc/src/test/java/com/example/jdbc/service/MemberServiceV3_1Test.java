package com.example.jdbc.service;

import static com.example.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.jdbc.domain.Member;
import com.example.repository.MemberRepositoryV2;
import com.example.repository.MemberRepositoryV3;

class MemberServiceV3_1Test {

	public static final String MEMBER_A = "memberA";
	public static final String MEMBER_B = "memberB";
	public static final String MEMBER_EX = "ex";

	private MemberRepositoryV3 memberRepository;

	private MemberServiceV3_1 memberService;

	@BeforeEach
	void before(){
		DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
		memberRepository = new MemberRepositoryV3(dataSource);
		PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
		// 기술 스택 변경시 주입되는 의존성 문제만 해결해주면 서비스 코드 수정 필요는 없어진다.
		memberService = new MemberServiceV3_1(transactionManager, memberRepository);

		// TransactionManager 또한 DataSource가 필요하다
	}

	@AfterEach
	void after() throws SQLException {
		// 리소스 정리
		memberRepository.delete(MEMBER_A);
		memberRepository.delete(MEMBER_B);
		memberRepository.delete(MEMBER_EX);
	}

	@Test
	@DisplayName("정상 이체")
	void accountTransfer() throws SQLException {
		// Given
		Member memberA = new Member(MEMBER_A, 10000);
		Member memberB = new Member(MEMBER_B, 10000);
		Member memberEx = new Member(MEMBER_EX, 10000);
		memberRepository.save(memberA);
		memberRepository.save(memberB);
		// When
		memberService.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);

		// Then
		Member findMemberA = memberRepository.findById(memberA.getMemberId());
		Member findMemberB = memberRepository.findById(memberB.getMemberId());

		assertThat(findMemberA.getMoney()).isEqualTo(8000);
		assertThat(findMemberB.getMoney()).isEqualTo(12000);

	}

	@Test
	@DisplayName("이체중 예외 발생")
	void accountTransferEx() throws SQLException {
		//given
		Member memberA = new Member("memberA", 10000);
		Member memberEx = new Member("ex", 10000);
		memberRepository.save(memberA);
		memberRepository.save(memberEx);
		//when
		assertThatThrownBy(() ->
			memberService.accountTransfer(memberA.getMemberId(), memberEx.getMemberId(),
				2000))
			.isInstanceOf(IllegalStateException.class);
		//then
		Member findMemberA = memberRepository.findById(memberA.getMemberId());
		Member findMemberEx = memberRepository.findById(memberEx.getMemberId());
		//memberA의 돈이 롤백 되어야함
		assertThat(findMemberA.getMoney()).isEqualTo(10000);
		assertThat(findMemberEx.getMoney()).isEqualTo(10000);
	}

}