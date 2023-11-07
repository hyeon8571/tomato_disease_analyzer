package tomato.classifier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tomato.classifier.domain.entity.Disease;


public interface DiseaseRepository extends JpaRepository<Disease, String> {

}
