
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Enrolment;

@Repository
public interface EnrolmentRepository extends JpaRepository<Enrolment, Integer> {

	@Query("select e from Enrolment e where e.dropOutMoment is null and e.registeredMoment is not null and e.brotherhood.id = ?1")
	Collection<Enrolment> findEnroledMembers(int brotherhoodId);

	@Query("select e from Enrolment e where e.dropOutMoment is null and e.registeredMoment is null and e.brotherhood.id = ?1")
	Collection<Enrolment> findRequestEnrolments(int brotherhoodId);

	@Query("select e from Enrolment e where e.dropOutMoment is null and e.registeredMoment is not null and e.member.id = ?1 and e.brotherhood.id = ?2")
	Enrolment findActiveEnrolment(int memberId, int brotherhoodId);

	@Query("select e from Enrolment e where e.dropOutMoment is not null and e.registeredMoment is not null and e.position is null and e.member.id = ?1 and e.brotherhood.id = ?2")
	Enrolment findInactiveEnrolment(int memberId, int brotherhoodId);

	@Query("select e from Enrolment e where e.dropOutMoment is null and e.registeredMoment is null and e.member.id = ?1 and e.brotherhood.id = ?2")
	Enrolment findRequestEnrolment(int memberId, int brotherhoodId);

	@Query("select e from Enrolment e where e.member.id = ?1 and e.registeredMoment is not null")
	Collection<Enrolment> findAllEnrolmentsByMemberId(int memberId);

}
