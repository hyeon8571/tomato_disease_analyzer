package tomato.classifier.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import tomato.classifier.domain.entity.ArticleLikes;
import tomato.classifier.domain.entity.QArticleLikes;

@Repository
public class ArticleLikesCustomRepositoryImpl implements ArticleLikesCustomRepository{

    private final JPAQueryFactory queryFactory;

    public ArticleLikesCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.queryFactory = jpaQueryFactory;
    }

    public ArticleLikes findLikes(String nickname, Long articleId) {
        QArticleLikes articleLikes = QArticleLikes.articleLikes;
        return queryFactory
                .select(articleLikes)
                .from(articleLikes)
                .where(articleLikes.article.articleId.eq(articleId), articleLikes.user.nickname.eq(nickname))
                .fetchOne();
    }
}
