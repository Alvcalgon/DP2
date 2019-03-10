
package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Parade;

@Repository
public interface ParadeRepository extends JpaRepository<Parade, Integer> {

	@Query("select p from Parade p where p.moment BETWEEN ?1 and ?2")
	Collection<Parade> findParadeLess30days(Date today, Date more30days);

	@Query("select p.ticker from Parade p where p.ticker = ?1")
	String existTicker(String ticker);

	@Query("select p from Parade p join p.floats f where f.id=?1")
	Collection<Parade> floatBelongtToParade(Integer id);

	@Query("select distinct p from Parade p join p.floats f where f.brotherhood.id=?1")
	Collection<Parade> findParadeByBrotherhood(int id);

	@Query("select distinct p from Parade p join p.floats f where f.brotherhood.id=?1 and p.isFinalMode=true and p.status='accepted'")
	Collection<Parade> findParadeVisibleByBrotherhood(int id);

	@Query("select avg(f.parades.size), min(f.parades.size), max(f.parades.size), stddev(f.parades.size) from Finder f")
	Double[] findDataNumberResultsPerFinder();

	@Query("select distinct p from Parade p join p.floats f where (p.isFinalMode = true) and ((p.ticker like concat('%', concat(?1, '%'))) or (p.title like concat('%', concat(?1, '%'))) or (p.description like concat('%', concat(?1, '%')))) and (f.brotherhood.area.name = ?2 or ?2 = '') and (p.moment >= ?3 or ?3 = NULL) and (p.moment <= ?4 or ?4 = NULL)")
	Page<Parade> searchParadeFinder(String keyword, String area, Date minimumDate, Date maximumDate, Pageable pageable);

	@Query("select p from Parade p where p.isFinalMode=true")
	Collection<Parade> findPublishedParade();
}
