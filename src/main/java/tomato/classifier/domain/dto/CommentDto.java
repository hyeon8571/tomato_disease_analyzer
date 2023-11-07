package tomato.classifier.domain.dto;

import lombok.*;
import tomato.classifier.domain.entity.Comment;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long commentId;

    private Long articleId;

    private String nickname;

    private Long parentCommentId;

    private Set<CommentDto> childComments;

    private String content;

    private boolean deleteYn;

    private boolean updateYn;

    private LocalDateTime modifiedTime;

    public static CommentDto of(Long articleId, String nickname, Long parentCommentId, String content) {
        return CommentDto.of(null, articleId, nickname, parentCommentId, content, false, false, null);
    }

    public static CommentDto of(Long commentId, Long articleId, String nickname, Long parentCommentId, String content, boolean deleteYn, boolean updateYn, LocalDateTime modifiedTime) {
        Comparator<CommentDto> childCommentComparator = Comparator
                .comparing(CommentDto::getModifiedTime)
                .thenComparing(CommentDto::getCommentId); // 오름차순
        return new CommentDto(commentId, articleId, nickname, parentCommentId, new TreeSet<>(childCommentComparator), content, deleteYn, updateYn, modifiedTime);
    }

    public static CommentDto toDto(Comment target) {

        return CommentDto.of(
                target.getCommentId(),
                target.getArticle().getArticleId(),
                target.getUser().getNickname(),
                target.getParentCommentId(),
                target.getContent(),
                target.isDeleteYn(),
                target.isUpdateYn(),
                target.getModifiedTime()
                );
    }

    public boolean hasParentComment() {
        return parentCommentId != null;
    }

}
