package tomato.classifier.repository.querydsl;

import tomato.classifier.domain.entity.ArticleLikes;

public interface ArticleLikesCustomRepository {

    ArticleLikes findLikes(String nickname, Long articleId);
}
