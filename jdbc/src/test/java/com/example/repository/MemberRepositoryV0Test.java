package com.example.repository;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.example.jdbc.domain.Member;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class MemberRepositoryV0Test {

	MemberRepositoryV0 repository = new MemberRepositoryV0();

	@Test
	void crud() throws SQLException {
		Member member = new Member("memberV122", 10000);
		repository.save(member);

		Member findMember = repository.findById(member.getMemberId());
		log.info("findMember = {}", findMember);
		// findMember와 member, equals는 왜 True?
		//
		Assertions.assertThat(findMember).isEqualTo(member);

		//update : money 10000 -> 20000
		repository.update(member.getMemberId(), 20000);
		Member updatedMember = repository.findById(member.getMemberId());
		Assertions.assertThat(updatedMember.getMoney()).isEqualTo(20000);

		repository.delete(member.getMemberId());
		Assertions.assertThatThrownBy(() -> repository.findById(member.getMemberId())).isInstanceOf(
			NoSuchElementException.class);
	}
}