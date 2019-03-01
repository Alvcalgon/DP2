
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {

	@Query("select count(e) from Enrolment e where e.position.id=?1")
	Integer isUsedPosition(int positionId);

	@Query("select (select count(e) from Enrolment e where e.position=p) from Position p")
	List<Integer> findHistogramValues();

	@Query("select tp.name from Position p join p.translationPositions tp where tp.language=?1")
	List<String> findHistogramLabels(String language);
}
