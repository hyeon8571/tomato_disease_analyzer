package tomato.classifier;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tomato.classifier.domain.entity.Role;
import tomato.classifier.domain.entity.User;
import tomato.classifier.repository.UserRepository;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class UserInit {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void memberInit(){
        userRepository.save(new User().builder()
                .loginId("zx8571")
                .nickname("user1")
                .email("testA@email.com")
                .password(passwordEncoder.encode("shvhsy8571"))
                .role(Role.USER)
                .username("원승현")
                .build());
    }

}
