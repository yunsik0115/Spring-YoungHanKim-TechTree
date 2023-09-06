package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("do log filter doFilter");
        // 고객의 요청이 올때마다 doFilter 호출

        // HttpServletRequest의 부모인 ServletRequest를 호출하나 기능이 거의 없어 다운캐스팅할것
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // Http요청말고 다른것도 받을 수 있도록 설계되었는데 다른거 안써서 다운캐스트 하는게 낫다

        String requestURI = httpRequest.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        try{
            log.info("REQUEST = [{}], [{}]", uuid, requestURI);
            chain.doFilter(request, response); // 있으면 다음 필터 없으면 서블릿 호출
        } catch (Exception e){
            throw e;
        }
        finally {
            log.info("RESPONSE = [{}] [{}]", uuid, requestURI); // 여기서 컨트롤러 왔다갔다 하는 시간 측정 가능
        }
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }


}
