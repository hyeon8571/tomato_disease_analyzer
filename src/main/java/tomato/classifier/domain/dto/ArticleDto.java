package tomato.classifier.domain.dto;

import lombok.*;
import tomato.classifier.domain.entity.Article;
import tomato.classifier.domain.entity.Comment;


import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ArticleDto {

    private Long articleId;

    private String title;

    private String content;

    private String nickname;

    private boolean deleteYn;

    private boolean updateYn;

    private LocalDateTime modifiedTime;

    private Set<CommentDto> comments;


    private Integer commentCount;

    private Integer likeNum;

    private Integer hateNum;


    @Builder
    public ArticleDto(Long articleId, String title, String content, String nickname, boolean deleteYn, boolean updateYn, LocalDateTime modifiedTime, Set<CommentDto> comments, Integer commentCount, Integer likeNum, Integer hateNum) {
        this.articleId = articleId;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.deleteYn = deleteYn;
        this.updateYn = updateYn;
        this.modifiedTime = modifiedTime;
        this.comments = comments;
        this.commentCount = commentCount;
        this.likeNum = likeNum;
        this.hateNum = hateNum;
    }


    public static ArticleDto toDto(Article target) {

        Integer count = 0;
        if(target.getComments() != null) {
            for (Comment comment : target.getComments()) {
                if (!comment.isDeleteYn()) {
                    count++;
                }
            }
        }

        if (target.getComments() != null) {
            Set<CommentDto> commentDtos =  target.getComments().stream()
                    .filter(comment -> comment.isDeleteYn() == false)
                    .map(CommentDto::toDto)
                    .collect(Collectors.toUnmodifiableSet());

            return ArticleDto.builder()
                    .articleId(target.getArticleId())
                    .title(target.getTitle())
                    .content(target.getContent())
                    .nickname(target.getUser().getNickname())
                    .deleteYn(target.isDeleteYn())
                    .updateYn(target.isUpdateYn())
                    .modifiedTime(target.getModifiedTime())
                    .comments(organizedComments(commentDtos))
                    .commentCount(count)
                    .likeNum(target.getLikeNum())
                    .hateNum(target.getHateNum())
                    .build();
        } else {
            return ArticleDto.builder()
                    .articleId(target.getArticleId())
                    .title(target.getTitle())
                    .content(target.getContent())
                    .nickname(target.getUser().getNickname())
                    .deleteYn(target.isDeleteYn())
                    .updateYn(target.isUpdateYn())
                    .modifiedTime(target.getModifiedTime())
                    .commentCount(count)
                    .likeNum(target.getLikeNum())
                    .hateNum(target.getHateNum())
                    .build();
        }
    }

    /**
     * parentCommentId가 존재하는 댓글은 오름차순(CommentDto 의 toDto 시 실행), 없는 댓글은 내림차순 정렬
     * -> 둘다 오름차순으로 변경
     */
    private static Set<CommentDto> organizedComments(Set<CommentDto> comments) {
        Map<Long, CommentDto> map = comments.stream()
                .collect(Collectors.toMap(CommentDto::getCommentId, Function.identity()));

        map.values().stream()
                .filter(CommentDto::hasParentComment)
                .forEach(comment -> {
                    CommentDto parentComment = map.get(comment.getParentCommentId());
                    parentComment.getChildComments().add(comment);
                });

        return map.values().stream()
                .filter(comment -> !comment.hasParentComment())
                .collect(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator
                                .comparing(CommentDto::getModifiedTime)
                                .thenComparingLong(CommentDto::getCommentId)
                        )
                        ));

    }

}