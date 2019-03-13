
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

	@Query("select avg(f.parades.size), min(f.parades.size), max(f.parades.size), stddev(f.parades.size) from Finder f")
	Double[] findDataNumberResultsPerFinder();

	@Query("select distinct p from Parade p join p.floats f where (p.isFinalMode = true) and ((p.ticker like concat('%', concat(?1, '%'))) or (p.title like concat('%', concat(?1, '%'))) or (p.description like concat('%', concat(?1, '%')))) and (f.brotherhood.area.name = ?2 or ?2 = '') and (p.moment >= ?3 or ?3 = NULL) and (p.moment <= ?4 or ?4 = NULL)")
	Page<Parade> searchParadeFinder(String keyword, String area, Date minimumDate, Date maximumDate, Pageable pageable);

	@Query("select p from Parade p where p.isFinalMode=true")
	Collection<Parade> findPublishedParade();

	@Query("select distinct p from Parade p join p.floats f where f.brotherhood.id=?1")
	Collection<Parade> findParadeByBrotherhood(int id);

	@Query("select distinct p from Parade p join p.floats f where f.brotherhood.id=?1 and p.isFinalMode= true")
	Collection<Parade> findParadeFinalByBrotherhood(int id);

	@Query("select distinct p from Parade p join p.floats f where f.brotherhood.id=?1 and p.status='submitted'")
	Collection<Parade> findParadeSubmittedByBrotherhood(int id);

	@Query("select distinct p from Parade p join p.floats f where f.brotherhood.id=?1 and  p.status='rejected'")
	Collection<Parade> findParadeRejectedByBrotherhood(int id);

	@Query("select distinct p from Parade p join p.floats f where f.brotherhood.id=?1 and p.status='accepted'")
	Collection<Parade> findParadeAcceptedByBrotherhood(int id);

	@Query("select distinct p from Parade p join p.floats f where f.brotherhood.id=?1 and p.isFinalMode=true and p.status='submitted'")
	Collection<Parade> findParadeSubmittedFinalByBrotherhood(int id);

	@Query("select distinct p from Parade p join p.floats f where f.brotherhood.id=?1 and p.isFinalMode=true and p.status='rejected'")
	Collection<Parade> findParadeRejectedFinalByBrotherhood(int id);

	@Query("select distinct p from Parade p join p.floats f where f.brotherhood.id=?1 and p.isFinalMode=true  and p.status ='accepted'")
	Collection<Parade> findParadeAcceptedFinalByBrotherhood(int id);

	@Query("select distinct p from Parade p join p.floats f where f.brotherhood.area.id=?1 and p.isFinalMode=true and p.status='submitted'")
	Collection<Parade> findSubmittedByArea(int id);

	@Query("select distinct p from Parade p join p.floats f where f.brotherhood.area.id=?1 and p.isFinalMode=true and p.status='rejected'")
	Collection<Parade> findRejectedByArea(int id);

	@Query("select distinct p from Parade p join p.floats f where f.brotherhood.area.id=?1 and p.isFinalMode=true  and p.status ='accepted'")
	Collection<Parade> findAcceptedByArea(int id);

	@Query("select p from Parade p join p.segments seg where seg.id=?1")
	Parade findBySegment(int segmentId);

	// Req 8.1.4 Acme-Parade Ratio parades in draft mode vs final mode
	@Query("select count(pa)/(select count(p) from Parade p where p.isFinalMode = true)*1.0 from Parade pa where pa.isFinalMode = false")
	Double findRatioParadesDraftModeVSParadesFinalMode();

	// Req 8.1.5 Acme-Parade status = 'submitted' 
	@Query("select (sum(case when p.status = 'submitted' then 1.0 else 0 end)/count(*)) from Parade p where p.isFinalMode = true")
	Double findRatioSubmittedParadesFinalMode();

	// Req 8.1.5 Acme-Parade status = 'accepted'
	@Query("select (sum(case when p.status = 'accepted' then 1.0 else 0 end)/count(*)) from Parade p where p.isFinalMode = true")
	Double findRatioAcceptedParadesFinalMode();

	// Req 8.1.5 Acme-Parade status = 'rejected'
	@Query("select (sum(case when p.status = 'rejected' then 1.0 else 0 end)/count(*)) from Parade p where p.isFinalMode = true")
	Double findRatioRejectedParadesFinalMode();

	// Req 8.1.2 Acme-Parade. Average, Min, Max, Stdev number of parades coordinated by the chapters
	@Query("select avg(1.0 * (select count(p) from Parade p join p.floats f where f.brotherhood.area.id = a.id)),min(1.0 * (select count(p) from Parade p join p.floats f where f.brotherhood.area.id = a.id)),max(1.0 * (select count(p) from Parade p join p.floats f where f.brotherhood.area.id = a.id)),stddev (1.0 * (select count(p) from Parade p join p.floats f where f.brotherhood.area.id = a.id)) from Area a where a in (select c.area from Chapter c)")
	Double[] findDataNumerParadesCoordinatedByChapters();

	@Query("select avg(1.0 * (select count(p) from Parade p join p.floats f where f.brotherhood.area.id = a.id)) from Area a where a in (select c.area from Chapter c)")
	Double avgNumberParadesCoordinatedByChapters();

}
