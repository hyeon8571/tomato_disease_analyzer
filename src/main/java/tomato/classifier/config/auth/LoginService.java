package tomato.classifier.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tomato.classifier.domain.entity.User;
import tomato.classifier.repository.UserRepository;

@Service
public class LoginService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // 시큐리티로 로그인이 될 때, 시큐리티가 loadUserByUsername() 실행해서 username을 체크
    // 체크해서 없으면 오류, 있으면 정상적으로 시큐리티 컨텍스트 내부에 로그인된 세션이 하나 만들어진다.
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException { // 로그인시 세션 만들어줌
        User userPS = userRepository.findByLoginId(loginId).orElseThrow(
                () -> new InternalAuthenticationServiceException("인증 실패")
        );
        return new LoginUser(userPS);
    }
}
