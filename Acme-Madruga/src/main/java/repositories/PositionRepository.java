
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {

	@Query("select count(e) from Enrolment e where e.position.id=?1")
	Integer isUsedPosition(int positionId);

	//TODO: Tomad los valores del histograma: Req 12.3.8
	// El problema de esta query es que no tiene en cuenta los cargos
	// que no son ocupados por ningun miembro.
	@Query("select count(e) from Enrolment e where e.position is not null group by e.position")
	Collection<Integer> findHistogramValues();

	@Query("select tp.name from Position p join p.translationPositions tp where tp.language=?1")
	Collection<String> findHistogramLabels(String language);
}
