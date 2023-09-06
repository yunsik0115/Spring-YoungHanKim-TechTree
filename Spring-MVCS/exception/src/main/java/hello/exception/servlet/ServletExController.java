package hello.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Slf4j
@Controller
public class ServletExController {

    @GetMapping("/error-ex")
    public void errorEx(){
        throw new RuntimeException("Exception Occurred");
    }

    // WAS까지 예외가면 WAS에서 다시 여기로 호출함.

    @GetMapping("/error-404")
    public void error404(HttpServletResponse response) throws IOException {
        // 발생하는 것은 아니고 메서드자체가 checked 예외로 IOException이 있어서 던져줘야함.
        response.sendError(404, "404 오류!");
    }

    @GetMapping("/error-500")
    public void error500(HttpServletResponse response) throws IOException {
        // Exception 터진건 무조건 500으로 나간다.
        // 아니면 직접 예외 상태코드를 아래처럼 만들어서 처리할 수 있다!
        response.sendError(500);
    }
}
