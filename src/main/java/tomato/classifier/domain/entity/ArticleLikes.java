package tomato.classifier.domain.entity;

import lombok.*;
import tomato.classifier.domain.dto.ArticleLikesDto;

import javax.persistence.*;

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class ArticleLikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "articleId")
    private Article article;

    @Column
    private Boolean status; // 좋아요 true, 싫어요 false

    public static ArticleLikes toEntity(ArticleLikesDto articleLikesDto, User user, Article article) {
        return new ArticleLikes().builder()
                .id(articleLikesDto.getId())
                .user(user)
                .article(article)
                .status(articleLikesDto.getRequestStatus())
                .build();
    }

}
