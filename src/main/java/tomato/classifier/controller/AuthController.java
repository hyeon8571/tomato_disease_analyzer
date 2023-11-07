package tomato.classifier.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import static tomato.classifier.domain.dto.user.UserReqDto.*;


@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginForm() {
        return "auth/login";
    }

    @GetMapping("/signup")
    public String signupForm(@ModelAttribute JoinReqDto joinReqDto) {
        return "auth/signup";
    }

    @GetMapping("/err-page")
    public String errPage() {
        return "message/unAuthenticatedMessage";
    }

    /**
     * 회원가입 후 로그인 시 document.referrer로 인해 /api/signup으로 get 요청이 들어오므로 처리
     */
    @GetMapping("/api/signup")
    public String redirect() {
        return "redirect:/";
    }

 }
