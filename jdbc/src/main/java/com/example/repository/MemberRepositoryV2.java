package com.example.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;

import javax.sql.DataSource;

import org.springframework.jdbc.support.JdbcUtils;

import com.example.jdbc.domain.Member;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberRepositoryV2 {

	private final DataSource dataSource;

	public MemberRepositoryV2(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Member save(Member member) throws SQLException {
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

	public Member findById(Connection con, String memberId) throws SQLException{
		String sql = "select * from member where member_id = ?";

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try{
			// con = getConnection(); - 새로운 커넥션을 물고오기 때문에, 해당 라인은 주석처리
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
			// connection은 여기서 닫지 않는다.
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(pstmt);

			// 서비스에서 연결 종료 필요, 특정 메서드에서 종료하면 안됨.
		}
	}

	public void update(String memberId, int money) throws SQLException {
		String sql = "update member set money=? where member_id=?";

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, money);
			pstmt.setString(2, memberId);

			int resultSize = pstmt.executeUpdate();
			log.info("resultSize = {}", resultSize);

		} catch(SQLException e){
			log.error("db error", e);
			throw e;
		} finally {
			close(con, pstmt, null);
		}

	}

	public void update(Connection con, String memberId, int money) throws SQLException {
		String sql = "update member set money=? where member_id=?";

		PreparedStatement pstmt = null;

		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, money);
			pstmt.setString(2, memberId);

			int resultSize = pstmt.executeUpdate();
			log.info("resultSize = {}", resultSize);

		} catch(SQLException e){
			log.error("db error", e);
			throw e;
		} finally {
			close(con, pstmt, null);
		}

	}

	public void delete(String memberId) throws SQLException{
		String sql = "delete from member where member_Id = ?";

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, memberId);

			int resultSize = pstmt.executeUpdate();
			log.info("resultSize = {}", resultSize);

		} catch(SQLException e){
			log.error("db error", e);
			throw e;
		} finally {
			close(con, pstmt, null);
		}

	}
	private void close(Connection con, Statement stmt, ResultSet rs){

		JdbcUtils.closeResultSet(rs);
		JdbcUtils.closeStatement(stmt);
		JdbcUtils.closeConnection(con);

	}

	private Connection getConnection() throws SQLException{
		Connection conn = dataSource.getConnection();
		log.info("get connection = {}, class = {}", conn, conn.getClass());
		return conn;
	}
}
