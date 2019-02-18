
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.TranslationPosition;

@Repository
public interface TranslationPositionRepository extends JpaRepository<TranslationPosition, Integer> {

}
