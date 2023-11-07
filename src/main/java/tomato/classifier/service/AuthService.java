package tomato.classifier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tomato.classifier.config.auth.LoginUser;

import tomato.classifier.domain.entity.User;
import tomato.classifier.handler.ex.CustomApiException;
import tomato.classifier.repository.UserRepository;

import javax.servlet.http.Cookie;

import static tomato.classifier.domain.dto.user.UserReqDto.*;
import static tomato.classifier.domain.dto.user.UserResDto.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void signup(JoinReqDto joinReqDto) {
        User user = joinReqDto.toEntity(passwordEncoder);
        userRepository.save(user);
    }

    @Transactional
    public LoginResDto findLoginUser(LoginUser loginUser) {
        String loginId = loginUser.getUsername();

        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomApiException("유저 조회를 실패했습니다."));

        return new LoginResDto(user);
    }

    public Cookie expireCookie(String tokenKey) {
        Cookie cookie = new Cookie(tokenKey, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }

}
