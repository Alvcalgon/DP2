
package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Procession;

@Repository
public interface ProcessionRepository extends JpaRepository<Procession, Integer> {

	@Query("select p from Procession p where p.moment BETWEEN ?1 and ?2")
	Collection<Procession> findProcessionLess30days(Date today, Date more30days);

	@Query("select p.ticker from Procession p where p.ticker = ?1")
	String existTicker(String ticker);

	@Query("select p from Procession p join p.floats f where f.id=?1")
	Collection<Procession> floatBelongtToProcession(Integer id);

	@Query("select min(f.processions.size), max(f.processions.size), avg(f.processions.size), stddev(f.processions.size) from Finder f")
	Double[] findDataNumberResultsPerFinder();

}
