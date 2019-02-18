
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {

	@Query("select count(e) from Enrolment e where e.position.id=?1")
	Integer isUsedPosition(int positionId);

}
