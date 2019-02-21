
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

	//TODO tenemos que modificarla para que compruebe que en vez de que el dropOutMoment sea null, compruebe si es anterior a la fecha existente en registeredMoment para asegurarnos si ese miembro ha vuelto a enrolarse en dicha hermandad
	@Query("select e.brotherhood from Enrolment e where e.member.id = ?1 and e.dropOutMoment=null")
	Collection<Brotherhood> findByMemberId(int memberId);

}
