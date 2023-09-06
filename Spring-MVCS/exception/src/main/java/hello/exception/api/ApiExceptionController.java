package hello.exception.api;

import hello.exception.exception.BadRequestException;
import hello.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
public class ApiExceptionController {

    @GetMapping("/api/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id){
        if(id.equals("ex")){
            throw new RuntimeException("잘못된 사용자");
        }

        if(id.equals("bad")){
            throw new IllegalArgumentException("잘못된 입력 값");
        }

        if(id.equals("user-ex"))
        {
            throw new UserException("사용자 오류");

            // 오류가 너무 많이 출력됨 (왔다갔다 복잡)
        }

        return new MemberDto(id, "hello " + id);
        // 500 서버 잘못
        // 400 클라이언트 오류
    }

    @GetMapping("/api/response-status-ex1")
    public String responseStatusEx1(){
        throw new BadRequestException();
        // 애노테이션, 동적변경에 어려움이 있음.
    }

    @GetMapping("/api/response-status-ex2")
    public String responseStatusEx2(){
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "error.bad", new IllegalArgumentException());
        // 상태코드와 오류메세지까지 한번에 해결할 수 있음
    }

    @GetMapping("/api/default-handler-ex")
    public String defaultException(@RequestParam Integer data){
        return "ok";
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String memberId;
        private String name;
    }
}
