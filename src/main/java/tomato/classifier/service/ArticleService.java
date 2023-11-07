package tomato.classifier.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tomato.classifier.domain.dto.ArticleDto;
import tomato.classifier.domain.dto.request.ArticleRequest;
import tomato.classifier.domain.entity.Article;
import tomato.classifier.domain.entity.User;
import tomato.classifier.domain.type.SearchType;
import tomato.classifier.handler.ex.CustomApiException;
import tomato.classifier.repository.ArticleRepository;
import tomato.classifier.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
        if (searchKeyword == null || searchKeyword.isBlank()) {
            return articleRepository.findAll(pageable).map(ArticleDto::toDto);
        }

        return switch (searchType) {
            case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::toDto);
            case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::toDto);
            case NICKNAME -> articleRepository.findByUser_NicknameContaining(searchKeyword, pageable).map(ArticleDto::toDto);
        };
    }

    @Transactional(readOnly = true)
    public ArticleDto show(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomApiException("게시글 조회를 실패했습니다."));

        ArticleDto articleDto = ArticleDto.toDto(article);

        return articleDto;
    }

    public ArticleDto create(ArticleDto articleDto) {

        articleDto.setDeleteYn(false);
        articleDto.setUpdateYn(false);

        String nickname = articleDto.getNickname();

        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new CustomApiException("유저 조회를 실패했습니다."));

        Article article = Article.toEntity(articleDto, user);

        Article created = articleRepository.save(article);

        return ArticleDto.toDto(created);
    }

    public ArticleDto update(Long articleId, ArticleRequest articleRequest) {

        Article target = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomApiException("게시글 조회를 실패했습니다."));

        target.patch(articleRequest);

        Article updated = articleRepository.save(target);

        return ArticleDto.toDto(updated);
    }

    public ArticleDto delete(Long articleId) {

        Article target = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomApiException("게시글 조회를 실패했습니다."));

        target.delete();

        Article deleted = articleRepository.save(target);

        ArticleDto dto = ArticleDto.toDto(deleted);

        return dto;
    }
}
