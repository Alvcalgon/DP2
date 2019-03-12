
package repositories;

import java.awt.print.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Sponsor;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, Integer> {

	@Query("select s from Sponsor s where s.userAccount.id=?1")
	Sponsor findByUserAccount(int userAccountId);

	// Req 18.2.c
	// 
	// select (select count(sp) from Sponsorship sp where sp.isActive=true and sp.sponsor.id=s.id order by count(sp) ASC) from Sponsor s
	@Query("select sp.sponsor from Sponsorship sp where sp.isActive=true group by sp.sponsor order by count(sp) DESC")
	Page<Sponsor> topFiveSponsors(Pageable page);

}
