package tomato.classifier.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tomato.classifier.domain.entity.ArticleLikes;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleLikesDto {

    private Long id;

    private String nickname;

    private Long articleId;

    private Boolean requestStatus;

    public static ArticleLikesDto toDto(ArticleLikes articleLikes) {

        return new ArticleLikesDto().builder()
                .id(articleLikes.getId())
                .nickname(articleLikes.getUser().getNickname())
                .articleId(articleLikes.getArticle().getArticleId())
                .requestStatus(articleLikes.getStatus())
                .build();
    }
}
