package jpabook.jpashop;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberRepositoryTest {
//
//    @Autowired
//    MemberRepository memberRepository;
//
//    @Test // TDD with Tab auto generates Intellij supports (User customization)
//    @Transactional // TEST CASE에 있는 경우 사용 이후 자동 ROLLBACK
//    @Rollback(false) // Using Rollback Annotation prevents auto rollback in test case.
//    public void testMember() throws Exception{
//        // Given
//        Member member = new Member();
//        member.setUsername("memberA");
//
//        // When
//        Long savedId = memberRepository.save(member);
//        Member findMember = memberRepository.find(savedId);
//
//        // Then
//        Assertions.assertThat(findMember).isEqualTo(member);
//        // True - findMember && member -> TRUE (Same persistent context)
//        // 1st cache
//    }
}