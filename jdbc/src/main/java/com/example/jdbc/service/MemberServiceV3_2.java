package com.example.jdbc.service;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import com.example.jdbc.domain.Member;
import com.example.repository.MemberRepositoryV3;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberServiceV3_2 {

	private final TransactionTemplate txTemplate;
	private final MemberRepositoryV3 memberRepository;

	public MemberServiceV3_2(PlatformTransactionManager transactionManager, MemberRepositoryV3 memberRepository) {
		this.txTemplate = new TransactionTemplate(transactionManager);
		// 빈 직접 주입해도 됨. Template은 그냥 클래스임(유연성이 없다)
		// PlatformTransactionManager를 주입받으면 유연성이 생긴다.
		this.memberRepository = memberRepository;
	}

	// Connection 관련 코드가 (JDBC) 서비스 코드에 위치해 있다.
	// JDBC 기술에 서비스 코드가 의존하고 있다.
	// 트랜잭션이 구현되어 있지만, JDBC 코드에 의존적임. (향후에 기술 변경시 영향)

	// 이 단위는 원자적으로 Commit 되거나 Rollback 되어야 한다.
	public void accountTransfer(String fromId, String toId, int money) throws SQLException {

		// 아래 코드 내부에서 (트랜잭션 시작) ~ 비즈니스 로직 시작 ~ 끝 ~ (롤백/커밋) 전부 처리됨.
		txTemplate.executeWithoutResult((status) ->  {
			try {
				bizLogic(fromId, toId, money);
			} catch (SQLException e) {
				throw new IllegalStateException(e);
			}
		});

		// 체크 예외 커밋, 언체크 예외는 롤백한다.

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

	private static void release(Connection con) {
		if (con != null){
			try {
				con.setAutoCommit(true);
				// 자동 커밋 모드(원래 기본값으로 돌려주어야 함), 풀에 들어갈때 Auto commit false 이면 문제 발생 가능
				// 다시 꺼낼 때,,, 기본 값인지 아닌지 모르기 때문(그냥 기본값으로 원래대로 써라)
				con.close();
			} catch (Exception e) {
				log.info("error", e); // exception의 경우 {} 사용 안함.
			}
		}
	}

	private static void validation(Member toMember) {
		if (toMember.getMemberId().equals("ex")){
			throw new IllegalStateException("이체 중 예외 발생");
		}
	}
}
