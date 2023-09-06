package hello.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Controller
public class ErrorPageController {

    // RequestDispatcher 상수화 되어있음

    public static final String ERROR_EXCEPTION =
            "javax.servlet.error.exception";
    public static final String ERROR_EXCEPTION_TYPE =
            "javax.servlet.error.exception_type";
    public static final String ERROR_MESSAGE = "javax.servlet.error.message";
    public static final String ERROR_REQUEST_URI =
            "javax.servlet.error.request_uri";
    public static final String ERROR_SERVLET_NAME =
            "javax.servlet.error.servlet_name";
    public static final String ERROR_STATUS_CODE =
            "javax.servlet.error.status_code";

    @RequestMapping("/error-page/404")
    public String errorPage404(HttpServletRequest request, HttpServletResponse response){
        log.info("errorPage 404");
        printErrorInfo(request);
        return "error-page/404";
    }

    @RequestMapping("/error-page/500")
    public String errorPage500(HttpServletRequest request, HttpServletResponse response){
        log.info("errorPage 500");
        printErrorInfo(request);
        return "error-page/500";
    }

    @RequestMapping(value = "/error-page/500", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Object>> errorPage500Api(
            HttpServletRequest request, HttpServletResponse response
    ){
        Map<String, Object> result = new HashMap<>();
        Exception ex = (Exception) request.getAttribute(ERROR_EXCEPTION);
        result.put("status", request.getAttribute(ERROR_STATUS_CODE));
        result.put("message", ex.getMessage());

        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        // 상태 코드 정의 필요(오류 이유)

        return new ResponseEntity<>(result, HttpStatus.valueOf(statusCode));
    }

    private void printErrorInfo(HttpServletRequest request){
        log.info("Error Exception :{} ", request.getAttribute(ERROR_EXCEPTION));
        log.info("Error Exception Type :{} ", request.getAttribute(ERROR_EXCEPTION_TYPE));
        log.info("Error Message :{} ", request.getAttribute(ERROR_MESSAGE));
        log.info("Error Request URI :{} ", request.getAttribute(ERROR_REQUEST_URI));
        log.info("Error Servlet Name :{} ", request.getAttribute(ERROR_SERVLET_NAME));
        log.info("Error Status Code :{} ", request.getAttribute(ERROR_STATUS_CODE));
        log.info("dispatchType :{} ", request.getDispatcherType());
    }


}
