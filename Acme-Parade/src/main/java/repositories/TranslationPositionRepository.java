
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.TranslationPosition;

@Repository
public interface TranslationPositionRepository extends JpaRepository<TranslationPosition, Integer> {

	@Query("select t from Position p join p.translationPositions t where p.id=?1 and t.language=?2")
	TranslationPosition findByLanguagePosition(int positionId, String language);

}
