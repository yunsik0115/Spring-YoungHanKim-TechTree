package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public Member login(String loginId, String password){
//        Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId);
//
//        Member member = findMemberOptional.get(); // 안에 있는게 꺼내져 나오고 없으면 예외 터짐
//
//        if(member.getPassword().equals(password)){
//            return member;
//        } else {
//            return null;
//        }
//-----------------------------------------------------------------------------------------------------
//        Optional<Member> byLoginId = memberRepository.findByLoginId(loginId);
//        byLoginId.filter(m -> m.getPassword().equals(password)).orElse(null);

        return memberRepository.findByLoginId(loginId).filter(m -> m.getPassword().equals(password)).orElse(null);
    }


}
