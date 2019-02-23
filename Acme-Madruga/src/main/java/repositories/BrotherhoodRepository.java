
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

	@Query("select e.brotherhood from Enrolment e where e.member.id = ?1 and e.dropOutMoment=null and e.registeredMoment is not null")
	Collection<Brotherhood> findByMemberId(int memberId);

	@Query("select e.brotherhood from Enrolment e where e.dropOutMoment is null and e.registeredMoment is not null group by e.brotherhood having count(e) = (select max(1.0 * (select count(ee) from Enrolment ee where ee.dropOutMoment is null and ee.registeredMoment is not null and ee.brotherhood.id = b.id group by ee.brotherhood)) from Brotherhood b)")
	Brotherhood findLargest();

	@Query("select e.brotherhood from Enrolment e where e.dropOutMoment is null and e.registeredMoment is not null group by e.brotherhood having count(e) = (select min(1.0 * (select count(ee) from Enrolment ee where ee.dropOutMoment is null and ee.registeredMoment is not null and ee.brotherhood.id = b.id group by ee.brotherhood)) from Brotherhood b)")
	Brotherhood findSmallest();

}
