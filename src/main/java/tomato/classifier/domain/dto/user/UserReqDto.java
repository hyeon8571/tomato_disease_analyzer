package tomato.classifier.domain.dto.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tomato.classifier.domain.entity.Role;
import tomato.classifier.domain.entity.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserReqDto {

    @Getter
    @Setter
    public static class LoginReqDto {
        @NotEmpty(message = "아이디를 입력하세요.")
        private String loginId;
        @NotEmpty(message = "비밀번호를 입력하세요.")
        private String password;
    }

    @Getter
    @Setter
    public static class JoinReqDto {
        // 유효성 검사
        // 영문, 숫자는 되고, 길이 최소 2~20자 이내
        @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$", message = "영문/숫자 2~20 이내로 작성해주세요")
        @NotEmpty(message = "아이디를 입력하세요.")
        private String loginId;

        // 길이 4~20
        @NotEmpty(message = "비밀번호를 입력하세요.")
        @Size(min = 4, max = 20)
        private String password;

        // 길이 4~20
        @NotEmpty(message = "비밀번호 확인을 위해 입력하세요.")
        @Size(min = 4, max = 20)
        private String password2;


        // 이메일 형식
        @NotEmpty
        @Pattern(regexp = "^[a-zA-Z0-9]{2,10}@[a-zA-Z0-9]{2,6}\\.[a-zA-Z]{2,3}$", message = "이메일 형식으로 작성해주세요")
        private String email;

        // 영어, 한글, 1~20
        @NotEmpty
        @Pattern(regexp = "^[a-zA-Z가-힣]{1,20}$", message = "한글/영문 1~20자 이내로 작성해주세요")
        private String username;

        @NotEmpty(message = "닉네임을 입력하세요")
        @Size(min = 4, max = 10)
        private String nickname;

        public User toEntity(BCryptPasswordEncoder passwordEncoder) {
            return User.builder()
                    .loginId(loginId)
                    .password(passwordEncoder.encode(password))
                    .username(username)
                    .email(email)
                    .username(username)
                    .nickname(nickname)
                    .role(Role.USER)
                    .build();
        }
    }
}