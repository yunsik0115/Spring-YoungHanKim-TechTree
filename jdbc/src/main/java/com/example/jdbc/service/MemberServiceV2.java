package com.example.jdbc.service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.example.jdbc.domain.Member;
import com.example.repository.MemberRepositoryV2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***
 * Transaction - Parameter 연동
 */
@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

	private final DataSource dataSource;

	private final MemberRepositoryV2 memberRepository;

	// 이 단위는 원자적으로 Commit 되거나 Rollback 되어야 한다.
	public void accountTransfer(String fromId, String toId, int money) throws SQLException {
		Connection con = dataSource.getConnection();
		try{
			bizLogic(con, fromId, toId, money);
			con.commit();
		} catch (Exception e) {
			con.rollback();
			throw new IllegalStateException(e);
		} finally {
			release(con);
		}
	}

	private void bizLogic(Connection con, String fromId, String toId, int money) throws SQLException {
		con.setAutoCommit(false);
		// 비즈니스 로직 수행
		Member fromMember = memberRepository.findById(con, fromId);
		Member toMember = memberRepository.findById(con, toId);

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
