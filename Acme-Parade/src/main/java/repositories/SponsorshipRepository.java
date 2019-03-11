
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Sponsorship;

@Repository
public interface SponsorshipRepository extends JpaRepository<Sponsorship, Integer> {

	// Req 18.2.a
	@Query("select count(s)*1.0/(select count(s1) from Sponsorship s1) from Sponsorship s where s.isActive=true")
	Double ratioActiveSponsorship();

	// Req 18.2.b
	@Query("select avg(1.0*(select count(sp) from Sponsorship sp where sp.sponsor.id=s.id and sp.isActive=true)), min(1.0*(select count(sp) from Sponsorship sp where sp.sponsor.id=s.id and sp.isActive=true)), max(1.0*(select count(sp) from Sponsorship sp where sp.sponsor.id=s.id and sp.isActive=true)), stddev(1.0*(select count(sp) from Sponsorship sp where sp.sponsor.id=s.id and sp.isActive=true)) from Sponsor s)")
	Double[] dataSponsorshipPerSponsor();

	@Query("select s from Sponsorship s where s.parade.id = ?1")
	List<Sponsorship> findByParadeId(final int paradeId);

	@Query("select s from Sponsorship s where s.sponsor.id = ?1")
	Collection<Sponsorship> findAllBySponsorId(int sponsorId);
}
