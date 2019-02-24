
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

	@Query("select m from Member m where m.userAccount.id=?1")
	Member findByUserAccount(int userAccountId);

	@Query("select e.member from Enrolment e where e.brotherhood.id=?1 and e.dropOutMoment is null and e.registeredMoment is not null")
	Collection<Member> findEnroledMemberByBrotherhood(int brotherhoodId);

	@Query("select avg(1.0 * (select count(e) from Enrolment e where e.dropOutMoment is null and e.registeredMoment is not null and e.brotherhood.id = b.id)), min(1.0 * (select count(e) from Enrolment e where e.dropOutMoment is null and e.registeredMoment is not null and e.brotherhood.id = b.id)), max(1.0 * (select count(e) from Enrolment e where e.dropOutMoment is null and e.registeredMoment is not null and e.brotherhood.id = b.id)), stddev(1.0 * (select count(e) from Enrolment e where e.dropOutMoment is null and e.registeredMoment is not null and e.brotherhood.id = b.id group by e.brotherhood)) from Brotherhood b")
	Double[] findDataNumberMembersPerBrotherhood();

}
