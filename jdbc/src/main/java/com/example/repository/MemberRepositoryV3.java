package com.example.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import com.example.jdbc.domain.Member;

import lombok.extern.slf4j.Slf4j;

/**
 * Transaction Manager With Data Utils. - Transaction Synchronization Manager
 * DataSourceUtils.getConnection()
 * DataSourceUtils.releaseConnection()
 */
@Slf4j
public class MemberRepositoryV3 {

	private final DataSource dataSource;

	public MemberRepositoryV3(DataSource dataSource) {
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
		DataSourceUtils.releaseConnection(con, dataSource);
		// 트랜잭션 동기화 사용하려면 DataSourceUtils의 사용이 필요함.
		// JdbcUtils.closeConnection(con);

		// 이 경우에는 Service에서 생성한 Connection이기 때문에 리포지토리에서 닫지 않음
		// 단, Repository에서 시작한 Transaction의 경우 알아서 닫음.
	}

	private Connection getConnection() throws SQLException{
		Connection con = DataSourceUtils.getConnection(dataSource);
		// Transaction Synchronization Manager 가 사용하는 Connection이 있다면 그것을 반환함.
		// 관리하는 커넥션이 없다면 새로운 커넥션을 생성하여 반환함.
		log.info("get connection = {}, class = {}", con, con.getClass());
		return con;
	}
}
