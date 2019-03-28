
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

	@Query("select b.area from Parade p join p.floats f join f.brotherhood b where p.id=?1")
	Area findAreaByParade(int idParade);

	// Acme Parade
	@Query("select a from Area a where a not in (select c.area from Chapter c)")
	Collection<Area> findAreasNotAssigned();

	// Ratio of areas that area not coordinated by any chapters
	@Query("select count(a)*1.0/(select count(a1) from Area a1) from Area a where a not in (select c.area from Chapter c)")
	Double ratioAreaWithoutChapter();

}
