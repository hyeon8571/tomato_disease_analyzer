package tomato.classifier.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tomato.classifier.domain.dto.ArticleDto;
import tomato.classifier.domain.dto.request.ArticleRequest;
import tomato.classifier.handler.ex.CustomApiException;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Article extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;

    @Column(nullable = false)
    private String title;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 10000)
    private String content;

    @Column
    private boolean deleteYn;

    @Column
    private boolean updateYn;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL) // article 엔티티의 comments 필드의 주인은 반대편(commnet 엔티티)의 article필드
    private Set<Comment> comments;

    @Builder
    public Article(Long articleId, String title, User user, String content, boolean deleteYn, boolean updateYn, Set<Comment> comments) {
        this.articleId = articleId;
        this.title = title;
        this.user = user;
        this.content = content;
        this.deleteYn = deleteYn;
        this.updateYn = updateYn;
        this.comments = comments;
    }

    public static Article toEntity(ArticleDto target, User user) {
        if(target.getArticleId() != null)
            throw new CustomApiException("DTO -> ENTITY 실패, Article");

        return Article.builder()
                .title(target.getTitle())
                .user(user)
                .content(target.getContent())
                .deleteYn(target.isDeleteYn())
                .updateYn(target.isUpdateYn())
                .build();
    }

    public void patch(ArticleRequest articleRequest) {

        this.title = articleRequest.getTitle();
        this.content = articleRequest.getContent();
        this.updateYn = true;

    }

    public void delete() {
        this.deleteYn = true;

        for (Comment comment : this.comments) {
            comment.delete();
        }
    }
}
