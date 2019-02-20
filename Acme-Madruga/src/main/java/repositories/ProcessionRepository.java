
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
}
