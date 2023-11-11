package tomato.classifier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tomato.classifier.domain.entity.ArticleLikes;

public interface ArticleLikesRepository extends JpaRepository<ArticleLikes, Long> {
}
