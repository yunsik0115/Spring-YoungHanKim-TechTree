package hello.login.web.filter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String whiteList[]
            = { "/" ,"/login", "/members/add", "/logout", "/css/*"};

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try{
            log.info("인증 체크 필터 시작 {}" , requestURI);

            if(isLoginCheckPath(requestURI)){
                log.info("인증 체크 로직 실행 = {}", requestURI);
                HttpSession session = httpRequest.getSession(false);
                if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null){
                    log.info("미인증 사용자 요청 = {}", requestURI);
                    // 로그인으로 리다이렉트
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI);
                    // 로그인을 했으면 다시 원래 가려했던 페이지로 왔으면 좋겠다는 뜻임
                    return; // 다음 서블릿이나 호출 안한다 -> sendRedirect로 보냄
                }

            }
            chain.doFilter(request, response);
        } catch (Exception e){
            throw e; // 예외 로깅 가능한데 톰캣까지 예외를 올려줘야 함
            // WAS 까지 안가는 경우, 서블릿 필터에서 올라오는걸 먹으면 정상처럼 동작함.
        } finally {
            log.info("인증 체크 필터 종료 {}", requestURI);
        }

    }

    /**
     * Whitelist Validation Override
     */

    private boolean isLoginCheckPath(String requestURI){

        return !PatternMatchUtils.simpleMatch(whiteList, requestURI);

    }
}
