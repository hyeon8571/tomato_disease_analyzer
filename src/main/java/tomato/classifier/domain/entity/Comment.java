package tomato.classifier.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tomato.classifier.domain.dto.CommentDto;
import tomato.classifier.domain.dto.request.CommentRequest;
import tomato.classifier.handler.ex.CustomApiException;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "articleId")
    private Article article;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @Column(updatable = false)
    private Long parentCommentId;

    @OrderBy("createdTime ASC")
    @OneToMany(mappedBy = "parentCommentId", cascade = CascadeType.ALL)
    private Set<Comment> childComments = new LinkedHashSet<>();

    @Column(nullable = false, length = 500)
    private String content;

    @Column
    private boolean deleteYn;

    @Column
    private boolean updateYn;

    @Builder
    public Comment(Long commentId, Article article, User user, Long parentCommentId, String content, boolean deleteYn, boolean updateYn) {
        this.commentId = commentId;
        this.article = article;
        this.user = user;
        this.parentCommentId = parentCommentId;
        this.content = content;
        this.deleteYn = deleteYn;
        this.updateYn = updateYn;
    }

    public static Comment toEntity(CommentDto commentDto, Article article, User user) {
        if (commentDto.getCommentId() != null) {
            throw new CustomApiException("Dto -> Entity 전환을 실패했습니다.");
        }

        if (commentDto.getArticleId() != article.getArticleId()) {
            throw new CustomApiException("Dto -> Entity 전환을 실패했습니다.");
        }

        return Comment.builder()
                .article(article)
                .user(user)
                .parentCommentId(commentDto.getParentCommentId())
                .content(commentDto.getContent())
                .deleteYn(commentDto.isDeleteYn())
                .updateYn(commentDto.isUpdateYn())
                .build();
    }

    public void patch(CommentRequest commentRequest) {

        this.content = commentRequest.getContent();
        this.updateYn = true;

    }

    public void delete() {
        this.deleteYn = true;
    }

    public void addChildComment(Comment child) {
        child.setParentCommentId(this.getCommentId());
        this.getChildComments().add(child);
    }

}
