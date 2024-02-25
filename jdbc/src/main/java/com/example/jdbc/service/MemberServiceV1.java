package com.example.jdbc.service;

import java.sql.SQLException;

import com.example.jdbc.domain.Member;
import com.example.repository.MemberRepositoryV1;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberServiceV1 {

	private final MemberRepositoryV1 memberRepository;

	// 이 단위는 원자적으로 Commit 되거나 Rollback 되어야 한다.
	public void accountTransfer(String fromId, String toId, int money) throws SQLException {
		// SQLException - JDBC 기술에 종속적인 예외에 해당.
		// 기술 바꾸게 되면 나중에 컴파일 과정에서 SQLException 변경 필요
		Member fromMember = memberRepository.findById(fromId);
		Member toMember = memberRepository.findById(toId);

		// 아래 두 동작은 같은 커넥션에서 동작해야 함 (같은 커넥션 - 같은 세션), 이를 위해 커넥션을 파라메터로 전달.
		memberRepository.update(fromId, fromMember.getMoney() - money);
		validation(toMember);
		memberRepository.update(toId, toMember.getMoney() + money);
		// 그 판단은 여기서 해야 함.
	}

	private static void validation(Member toMember) {
		if (toMember.getMemberId().equals("ex")){
			throw new IllegalStateException("이체 중 예외 발생");
		}
	}
}
