
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Brotherhood;

@Repository
public interface BrotherhoodRepository extends JpaRepository<Brotherhood, Integer> {

	@Query("select b from Brotherhood b where b.userAccount.id=?1")
	Brotherhood findByUserAccount(int userAccountId);

	@Query("select distinct f.brotherhood from Procession p join p.floats f where p.id= ?1")
	Brotherhood findBrotherhoodByProcession(int processionId);

	@Query("select e.brotherhood from Enrolment e where e.member.id = ?1")
	Collection<Brotherhood> findByMemberId(int memberId);

}
