package tomato.classifier.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tomato.classifier.domain.dto.ArticleDto;
import tomato.classifier.domain.dto.ArticleLikesDto;
import tomato.classifier.domain.dto.request.ArticleRequest;
import tomato.classifier.domain.dto.user.UserResDto;
import tomato.classifier.domain.entity.Article;
import tomato.classifier.domain.entity.ArticleLikes;
import tomato.classifier.domain.entity.User;
import tomato.classifier.domain.type.SearchType;
import tomato.classifier.handler.ex.CustomApiException;
import tomato.classifier.repository.ArticleLikesRepository;
import tomato.classifier.repository.ArticleRepository;
import tomato.classifier.repository.UserRepository;
import tomato.classifier.repository.querydsl.ArticleLikesCustomRepository;

import static tomato.classifier.domain.dto.user.UserResDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final UserRepository userRepository;

    private final ArticleLikesCustomRepository articleLikesCustomRepository;

    private final ArticleLikesRepository articleLikesRepository;

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

        articleDto.setLikeNum(0);
        articleDto.setHateNum(0);

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

    public ArticleLikesDto likeOrHate(ArticleLikesDto articleLikesDto) {

        User user = userRepository.findByNickname(articleLikesDto.getNickname())
                .orElseThrow(() -> new CustomApiException("유저 조회를 실패했습니다."));

        Article article = articleRepository.findById(articleLikesDto.getArticleId())
                .orElseThrow(() -> new CustomApiException("게시글 조회를 실패했습니다."));

        ArticleDto beforeArticleDto = ArticleDto.toDto(article);

        Integer likeNum = article.getLikeNum();
        Integer hateNum = article.getHateNum();

        Boolean requestStatus = articleLikesDto.getRequestStatus();


        ArticleLikesDto savedArticleLikesDto = checkExist(articleLikesDto);


        if (savedArticleLikesDto == null) {
            if (requestStatus) {
                beforeArticleDto.setLikeNum(++likeNum);
            } else {
                beforeArticleDto.setHateNum(++hateNum);
            }
        } else { // DB에 저장된 이력이 있는 경우
            Boolean savedRequest = savedArticleLikesDto.getRequestStatus();
            if (requestStatus) { // 프론트에서 넘어온 값이 좋아요
                if (savedRequest == null) { // 저장된 값 null

                    beforeArticleDto.setLikeNum(++likeNum);

                } else if (savedRequest) { // 저장된 값 true

                    beforeArticleDto.setLikeNum(--likeNum);

                    articleLikesDto.setRequestStatus(null);

                } else {

                    beforeArticleDto.setLikeNum(++likeNum);

                    beforeArticleDto.setHateNum(--hateNum);

                }
            } else { // 프론트에서 넘어온 값이 싫어요
                if (savedRequest == null) { // 저장된 값 null

                    beforeArticleDto.setHateNum(++hateNum);

                } else if (savedRequest) { // 저장된 값 true

                    beforeArticleDto.setHateNum(++hateNum);

                    beforeArticleDto.setLikeNum(--likeNum);

                } else {

                    beforeArticleDto.setHateNum(--hateNum);

                    articleLikesDto.setRequestStatus(null);
                }
            }
            articleLikesDto.setId(savedArticleLikesDto.getId());
        }

        Article afterArticle = Article.toEntity(beforeArticleDto, user);

        articleRepository.save(afterArticle);
        articleLikesRepository.save(ArticleLikes.toEntity(articleLikesDto, user, afterArticle));

        return articleLikesDto;
    }

    public ArticleLikesDto checkExist(ArticleLikesDto articleLikesDto) {

        ArticleLikes articleLikes = articleLikesCustomRepository.findLikes(articleLikesDto.getNickname(), articleLikesDto.getArticleId());

        if (articleLikes == null) {
            return null;
        } else {
            return ArticleLikesDto.toDto(articleLikes);
        }
    }

    public ArticleLikesDto getLikeOrHateInfo(LoginResDto loginResDto, Long articleId) {

        User user = userRepository.findByNickname(loginResDto.getNickname())
                .orElseThrow(() -> new CustomApiException("유저 조회를 실패했습니다."));

        ArticleLikesDto result = new ArticleLikesDto();

        for (ArticleLikes articleLike : user.getArticleLikesList()) {
            if (articleLike.getArticle().getArticleId().equals(articleId)) {
                result = ArticleLikesDto.toDto(articleLike);
            }
        }
        return result;
    }

}

