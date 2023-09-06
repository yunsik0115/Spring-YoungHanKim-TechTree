package hello.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>();
    private static Long sequence = 0L;

    public Member save(Member member){
        member.setId(++sequence);
        log.info("save : member = {}", member);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id){
        return store.get(id);
    }

    public Optional<Member> findByLoginId(String loginId){ // NULL로 반환하는 상황에서 사용
//        List<Member> all = findAll();
//        for (Member m : all) {
//            if(m.getLoginId().equals(loginId)){
//                return Optional.of(m);
//            }
//        }
//        return Optional.empty();

        return findAll().stream().filter(m -> m.getLoginId().equals(loginId)).findFirst();
        // 리스트를 스트림으로 바꿈 -> 그 조건에 만족하는 애만 다음단계로 넘어감 -> member에 loginId가 같으면 거기서 먼저 나오는 애를 가져다 반환함.
        // 뒤에 애들은 무시됨 자바 8 람다 스트림은 기본으로 쓸 수 있어야 한다!
    }

    public List<Member> findAll(){
        return new ArrayList<>(store.values());
        // value만 쭉 뽑아서 리스트 타입으로 바꿔서 반환
    }

    public void clearStore(){
        store.clear();
    }
}
