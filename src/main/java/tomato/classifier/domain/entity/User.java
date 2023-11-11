package tomato.classifier.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String loginId;

    @Column(nullable = false, length = 100) // 패스워드 인코딩(BCrypt)시 늘어나서 60자로 줌
    private String password;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(unique = true, nullable = false, length = 10)
    private String nickname;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Article> articles;

    @Column
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ArticleLikes> articleLikesList;


    @Builder
    public User(Long id, String loginId, String password, String username, String nickname, String email, Role role, List<Article> articles, List<ArticleLikes> articleLikesList) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
        this.articles = articles;
        this.articleLikesList = articleLikesList;
    }

}
