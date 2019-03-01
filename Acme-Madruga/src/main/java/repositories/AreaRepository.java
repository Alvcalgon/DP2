
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Area;

@Repository
public interface AreaRepository extends JpaRepository<Area, Integer> {

	@Query("select count (b) from Brotherhood b where b.area.id=?1")
	Integer findBrotherhoodFromArea(int i);

	@Query("select a.name from Area a")
	Collection<String> findAllAreaNames();

}
