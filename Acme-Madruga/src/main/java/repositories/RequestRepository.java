
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

	@Query("select r from Request r where r.member.id =?1 and r.procession.id=?2")
	Collection<Request> findRequestByMemberProcession(int id, int processionId);

	@Query("select r from Request r where r.member.id = ?1")
	Collection<Request> findRequestByMember(int id);

}
