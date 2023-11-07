package tomato.classifier.domain.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tomato.classifier.domain.dto.CommentDto;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequest {

    private Long articleId;

    private String nickname;

    private Long parentCommentId;

    private String content;


    @Builder
    public CommentRequest(Long articleId, Long parentCommentId, String content, String nickname) {
        this.articleId = articleId;
        this.nickname = nickname;
        this.parentCommentId = parentCommentId;
        this.content = content;
    }

    public CommentDto toDto() {
        return CommentDto.of(
                articleId,
                nickname,
                parentCommentId,
                content
        );
    }
}
