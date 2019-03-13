
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> {

	@Query("select distinct h from History h where h.brotherhood.id=?1")
	History findHistoryByBrotherhood(int brotherhoodId);

}
