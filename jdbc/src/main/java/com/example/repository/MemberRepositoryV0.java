package com.example.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;

import com.example.jdbc.connection.DBConnectionUtil;
import com.example.jdbc.domain.Member;

import lombok.extern.slf4j.Slf4j;

@Slf4j
// JDBC - DriverManager 사용해보기
public class MemberRepositoryV0 {

	public Member save(Member member) throws SQLException{
		String sql = "insert into member(member_id, money) values (?, ?)";

		Connection con = null;
		PreparedStatement pstmt = null; // Statement에 Parameter 바인딩 하는 기능의 추가

		try {
			con = getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, member.getMemberId());
			pstmt.setInt(2, member.getMoney());

			int i = pstmt.executeUpdate();// 쿼리가 실제 DB에 업데이트 됨.
			// i - 는 영향받은 row의 개수를 반환함.

			return member;
		} catch(SQLException e){
			log.error("db error", e);
			throw e;
		} finally {
			close(con, pstmt, null);
		}

	}

	public Member findById(String memberId) throws SQLException{
		String sql = "select * from member where member_id = ?";
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try{
			con = getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, memberId);
			rs = pstmt.executeQuery();
			if(rs.next()){ // 처음에는 아무것도 가리키지 않음.
				// next 호출시 첫번째 데이터를 방향하게 됨.
				Member member = new Member();
				member.setMemberId(rs.getString("member_id"));
				member.setMoney(rs.getInt("money"));
				return member;
			} else{
				throw new NoSuchElementException("member not found memberId=" + memberId);
			}

		} catch (SQLException e){
			log.error("db error", e);
			throw e;
		} finally {
			close(con, pstmt, rs);
		}
	}

	private void close(Connection con, Statement stmt, ResultSet rs){

		// 단순히 두줄로 close 하는 경우 하나가 close 되지 않는 문제 발생가능(예외 터지는 경우)

		if(rs != null){
			try{
				rs.close();
			} catch (SQLException e){
				log.info("error", e);
			}
		}

		if(stmt != null) {
			try {
				stmt.close();
			} catch(SQLException e){
				log.info("error", e);
			}
		}

		if(con != null){
			try{
				con.close();
			} catch (SQLException e){
				log.info("error", e);
			}
		}

	}

	private static Connection getConnection() {
		return DBConnectionUtil.getConnection();
	}

}
