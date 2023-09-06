package hello.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "잘못된 요청 오류")
// 2. 두 개 값 꺼내서 sendError하고 ModelAndView로 return 해버린다.
public class BadRequestException extends RuntimeException {
    // 1. ResponseStatusExceptionResolver에 의해 걸림
}