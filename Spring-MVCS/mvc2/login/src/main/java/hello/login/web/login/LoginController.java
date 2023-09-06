package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm loginForm){
        return "login/loginForm";
    }

    //@PostMapping("/login")
    public String loginFormV1(@Validated LoginForm loginForm, BindingResult bindingResult, HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }

        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());

        if(loginMember == null){
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        // 로그인 처리 TODO 쿠키를 만들어서 브라우저로 전송하면 브라우저에서 요청시마다 쿠키를 함께 전달함

        // 쿠키에 시간 정보를 주지 않으면 세션쿠키로 자동 설정됨.
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(idCookie);
        // HttpServletResponse가 쿠키 설정을 위해 필요함.

        return "redirect:/";

    }

    //@PostMapping("/login")
    public String loginFormV2(@Validated LoginForm loginForm, BindingResult bindingResult, HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }

        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());

        if(loginMember == null){
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        // 로그인 처리 TODO 쿠키를 만들어서 브라우저로 전송하면 브라우저에서 요청시마다 쿠키를 함께 전달함

        // 쿠키에 시간 정보를 주지 않으면 세션쿠키로 자동 설정됨.
        //Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        //response.addCookie(idCookie);
        // HttpServletResponse가 쿠키 설정을 위해 필요함.

        sessionManager.createSession(loginMember, response);

        return "redirect:/";

    }

    //@PostMapping("/login")
    public String loginFormV3(@Validated LoginForm loginForm,
                              BindingResult bindingResult,
                              HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }

        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());

        if(loginMember == null){
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        // 로그인 처리 TODO 쿠키를 만들어서 브라우저로 전송하면 브라우저에서 요청시마다 쿠키를 함께 전달함

        // 스프링에서 제공하는 HTTP 세션 매니저 사용
        HttpSession session = request.getSession(); // 세션이 있으면 있는 세션 반환 없으면 새로 만들어서 반환함
        // 세션에 로그인 회원 정보를 보관한다.
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        // 기본적으로 다 메모리에 저장됨

        // 세션 생성하려면 .getSession에 파라미터를 true(근데 디폴트라 생략가능)
        // false라고 하면 새로운 세션을 반환하지 않고 null로 반환한다.


        //sessionManager.createSession(loginMember, response);

        return "redirect:/";

    }

    @PostMapping("/login")
    public String loginFormV4(@Validated LoginForm loginForm,
                              BindingResult bindingResult,
                              @RequestParam(defaultValue = "/") String redirectURL,
                              HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }

        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());

        if(loginMember == null){
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        // 로그인 처리 TODO 쿠키를 만들어서 브라우저로 전송하면 브라우저에서 요청시마다 쿠키를 함께 전달함

        // 스프링에서 제공하는 HTTP 세션 매니저 사용
        HttpSession session = request.getSession(); // 세션이 있으면 있는 세션 반환 없으면 새로 만들어서 반환함
        // 세션에 로그인 회원 정보를 보관한다.
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        // 기본적으로 다 메모리에 저장됨

        // 세션 생성하려면 .getSession에 파라미터를 true(근데 디폴트라 생략가능)
        // false라고 하면 새로운 세션을 반환하지 않고 null로 반환한다.


        //sessionManager.createSession(loginMember, response);

        return "redirect:" + redirectURL;

    }

    //@PostMapping("/logout")
    public String logout(HttpServletResponse response){
        expireCookie(response);
        return "redirect:/";
    }

    //@PostMapping("/logout")
    public String logoutV2(HttpServletRequest request){
        sessionManager.expire(request);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        session.invalidate();
        return "redirect:/";
    }

    private void expireCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("memberId", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        // 클라이언트에 정보 저장시 뭐든 문제가 생길 수 있음 쿠키 위변조 또는 탈취시 큰 문제가 생긴다.
        // 전송 관련한 문제는 HTTPS인데 PC가 털려버리면 답이 없다.
        // 네트워크 전송구간에서 왔다갔다하는게 보여서 털릴 수 있음.
    }


}
