package com.example.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.example.jdbc.domain.Member;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class MemberRepositoryV0Test {

	MemberRepositoryV0 repository = new MemberRepositoryV0();

	@Test
	void crud() throws SQLException {
		Member member = new Member("memberV11", 10000);
		repository.save(member);

		Member findMember = repository.findById(member.getMemberId());
		log.info("findMember = {}", findMember);
		// findMember와 member, equals는 왜 True?
		//
		Assertions.assertThat(findMember).isEqualTo(member);
	}
}