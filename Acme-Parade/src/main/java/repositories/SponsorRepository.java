
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Sponsor;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, Integer> {

	@Query("select s from Sponsor s where s.userAccount.id=?1")
	Sponsor findByUserAccount(int userAccountId);

	// Req 18.2.c
	//	@Query("select s.name,(select count(sp) from Sponsorship sp where sp.isActive=true and sp.sponsor.id=s.id order by count(sp) ASC) from Sponsor s")
	//	Page<Sponsor> topFiveSponsors(Pageable page);

}
