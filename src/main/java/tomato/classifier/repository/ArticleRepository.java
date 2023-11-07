package tomato.classifier.repository;

import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import tomato.classifier.domain.entity.Article;
import tomato.classifier.domain.entity.QArticle;


public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        QuerydslPredicateExecutor<Article>
//        QuerydslBinderCustomizer<QArticle>
{ // 기본 검색 외에도 검색 기능을 만들기 위해 사용

    Page<Article> findByTitleContaining(String title, Pageable pageable);
    Page<Article> findByContentContaining(String content, Pageable pageable);
    Page<Article> findByUser_NicknameContaining(String nickname, Pageable pageable);

    // 인터페이에서 구현이 가능하게 하기 위해 default 추가
//    @Override
//    default void customize(QuerydslBindings bindings, QArticle root) {
//        bindings.excludeUnlistedProperties(true); // 선택된 인자만 검색에 이용할 수 있게 true
//        bindings.including(root.title, root.content, root.updatedTime);
//        bindings.bind(root.title).first(StringExpression::containsIgnoreCase); // like '%${v}%' 대소문자 무시 + 부분 검색
//        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
//        bindings.bind(root.updatedTime).first(DateTimeExpression::eq);
//    };
}
