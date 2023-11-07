package tomato.classifier.domain.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tomato.classifier.domain.entity.User;

public class UserResDto {

    @Getter
    @Setter
    public static class LoginResDto {
        private String loginId;
        private String nickname;

        public LoginResDto(User user) {
            this.loginId = user.getLoginId();
            this.nickname = user.getNickname();
        }
    }

    @ToString
    @Getter
    @Setter
    public static class JoinResDto {

        private String loginId;
        private String username;
        private String nickname;

        public JoinResDto(User user) {
            this.loginId = user.getLoginId();
            this.username = user.getUsername();
            this.nickname = user.getNickname();
        }
    }
}