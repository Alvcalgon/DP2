
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
	Collection<Request> findRequestByMemberId(int id);

	@Query("select r from Request r where r.member.id = ?1 and r.status='PENDING'")
	Collection<Request> findPendingRequestByMember(int id);

	@Query("select r from Request r where r.member.id = ?1 and r.status='APPROVED'")
	Collection<Request> findApprovedRequestByMember(int id);

	@Query("select r from Request r where r.member.id = ?1 and r.status='REJECTED'")
	Collection<Request> findRejectedRequestByMember(int id);

	@Query("select distinct r from Request r join r.procession p join p.floats f where f.brotherhood.id = ?1 and r.status='PENDING'")
	Collection<Request> findPendingRequestByBrotherhood(int id);

	@Query("select distinct r from Request r join r.procession p join p.floats f where f.brotherhood.id = ?1 and r.status='APPROVED'")
	Collection<Request> findApprovedRequestByBrotherhood(int id);

	@Query("select distinct r from Request r join r.procession p join p.floats f where f.brotherhood.id = ?1 and r.status='REJECTED'")
	Collection<Request> findRejectedRequestByBrotherhood(int id);

	@Query("select r from Request r where r.procession.id=?1")
	Collection<Request> findRequestByProcession(int processionId);

}
