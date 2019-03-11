
package repositories;

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
	@Query("select avg(1.0 * (select count(b) from Brotherhood b where b.area.id = a.id)), min(1.0 * (select count(b) from Brotherhood b where b.area.id = a.id)), max(1.0 * (select count(b) from Brotherhood b where b.area.id = a.id)), stddev(1.0 * (select count(b) from Brotherhood b where b.area.id = a.id)) from Area a)")
	Double[] dataSponsorshipPerSponsor();

}
