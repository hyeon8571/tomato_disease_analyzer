package tomato.classifier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tomato.classifier.domain.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

   @Query(value = "select * from Comment where article_id = :articleId", nativeQuery = true)
   List<Comment> findAllByArticleId(Long articleId);
}
