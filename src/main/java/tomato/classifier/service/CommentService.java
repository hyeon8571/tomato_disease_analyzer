package tomato.classifier.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tomato.classifier.domain.dto.CommentDto;
import tomato.classifier.domain.dto.request.CommentRequest;
import tomato.classifier.domain.entity.Article;
import tomato.classifier.domain.entity.Comment;
import tomato.classifier.domain.entity.User;
import tomato.classifier.handler.ex.CustomApiException;
import tomato.classifier.repository.ArticleRepository;
import tomato.classifier.repository.CommentRepository;
import tomato.classifier.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class CommentService {

    private final ArticleRepository articleRepository;

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    public List<CommentDto> comments(Long articleId) {

        List<Comment> allComments = commentRepository.findAllByArticleId(articleId);

        List<CommentDto> allCommentDtos = allComments
                .stream()
                .map(comment -> CommentDto.toDto(comment))
                .collect(Collectors.toList());

        List<CommentDto> comments = new ArrayList<>();

        allCommentDtos.stream()
                .filter(commentDto -> commentDto.isDeleteYn() == false)
                .forEach(commentDto -> comments.add(commentDto));

        return comments;
    }

    public CommentDto create(CommentDto commentDto) {

        Article article = articleRepository.findById(commentDto.getArticleId())
                .orElseThrow(() -> new CustomApiException("게시글 조회를 실패했습니다."));

        String nickname = commentDto.getNickname();

        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new CustomApiException("유저 조회를 실패했습니다."));

        Comment comment = Comment.toEntity(commentDto, article, user);

        if (commentDto.getParentCommentId() != null) {
            Comment parentComment = commentRepository.getReferenceById(commentDto.getParentCommentId());
            parentComment.addChildComment(comment);
            log.info("childComments: {}", parentComment.getChildComments().stream().map(commenta -> commenta.getContent()));
        } else {
            commentRepository.save(comment);
        }
        return CommentDto.toDto(comment);
    }

    public CommentDto update(Long commentId, CommentRequest commentRequest) {

        Comment target = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomApiException("댓글 조회를 실패했습니다."));

        target.patch(commentRequest);

        Comment updated = commentRepository.save(target);

        return CommentDto.toDto(updated);
    }

    public CommentDto delete(Long commentId) {

        Comment target = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomApiException("댓글 조회를 실패했습니다."));

        target.delete();

        Comment deleted = commentRepository.save(target);

        return CommentDto.toDto(deleted);
    }
}
