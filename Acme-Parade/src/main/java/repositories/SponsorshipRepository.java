
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Sponsorship;

@Repository
public interface SponsorshipRepository extends JpaRepository<Sponsorship, Integer> {

	@Query("select s from Sponsorship s where s.parade.id = ?1")
	List<Sponsorship> findByParadeId(final int paradeId);

	@Query("select s from Sponsorship s where s.sponsor.id = ?1")
	Collection<Sponsorship> findAllBySponsorId(int sponsorId);

}
