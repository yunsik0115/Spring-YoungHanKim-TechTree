package com.example.jdbc.service;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import com.example.jdbc.domain.Member;
import com.example.repository.MemberRepositoryV3;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberServiceV3_3 {

	private final MemberRepositoryV3 memberRepository;

	public MemberServiceV3_3(MemberRepositoryV3 memberRepository) {
		this.memberRepository = memberRepository;
	}

	@Transactional // 메서드 호출시 트랜잭션 걸고 시작하겠다. (AOP)
	public void accountTransfer(String fromId, String toId, int money) throws SQLException {

		bizLogic(fromId, toId, money);

	}

	private void bizLogic(String fromId, String toId, int money) throws SQLException {
		// 비즈니스 로직 수행
		Member fromMember = memberRepository.findById(fromId);
		Member toMember = memberRepository.findById(toId);

		// 아래 두 동작은 같은 커넥션에서 동작해야 함 (같은 커넥션 - 같은 세션), 이를 위해 커넥션을 파라메터로 전달.
		memberRepository.update(fromId, fromMember.getMoney() - money);
		validation(toMember);
		memberRepository.update(toId, toMember.getMoney() + money);
	}

	private static void validation(Member toMember) {
		if (toMember.getMemberId().equals("ex")){
			throw new IllegalStateException("이체 중 예외 발생");
		}
	}
}
