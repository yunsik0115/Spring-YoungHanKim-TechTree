package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional // JPA's all data flow must be done within @Transactional
//@AllArgsConstructor
@RequiredArgsConstructor // Only final field.
public class MemberService {

    private final MemberRepository memberRepository;

//    @Autowired // Test Code Mock Injection Available.
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    } // do not use setters (Unexpected Swap might be done)
    // can be done with allargsconstructor

    // Member Join
    public Long join(Member member){
        // name should be unique
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    // WAS 동시에 여러개 뜰때,,, DB 동시에 Insert 하게 되면...?
    // Last Resort - DB Unique constraint on Name column
    private void validateDuplicateMember(Member member){
        // Exception Will Be Thrown
        if(!((memberRepository.findByName(member.getName())).isEmpty())){
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }

    public void update(Long memberId, String name){
        Member member = memberRepository.findOne(memberId);
        member.setName(name);
    }

    // Retrieve All Members

    @Transactional(readOnly = true) // JPA Optimization for ReadOnly Transaction
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Member findOne(Long id){
        return memberRepository.findOne(id);
    }

}
