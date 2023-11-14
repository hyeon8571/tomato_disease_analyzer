package tomato.classifier.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tomato.classifier.config.jwt.JwtVO;
import tomato.classifier.repository.UserRepository;
import tomato.classifier.service.AuthService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static tomato.classifier.domain.dto.user.UserReqDto.*;


@RequiredArgsConstructor
@RequestMapping("/api")
@Controller
public class AuthApiController {

    private final AuthService authService;

    private final UserRepository userRepository;

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute JoinReqDto joinReqDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }

        if (!joinReqDto.getPassword().equals(joinReqDto.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordIncorrect", "패스워드가 일치하지 않습니다.");
            return "auth/signup";
        }

        if (userRepository.findByLoginId(joinReqDto.getLoginId()).isPresent()) {
            bindingResult.rejectValue("loginId", "idDuplication", "이미 존재하는 ID 입니다.");
            return "auth/signup";
        }

        if (userRepository.findByNickname(joinReqDto.getNickname()).isPresent()) {
            bindingResult.rejectValue("nickname", "nicknameDuplication", "이미 존재하는 닉네임 입니다.");
            return "auth/signup";
        }

        authService.signup(joinReqDto);

        return "message/signupMessage";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = authService.expireCookie(JwtVO.HEADER);
        System.out.println(cookie.getName());
        response.addCookie(cookie);
        return "redirect:/";
    }

}
