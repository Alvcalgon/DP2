
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

	//Req 12.3.4 (The ratio of requests to march in a procession, grouped by status: ('PENDING'))
	@Query("select (sum(case when r.status='PENDING' then 1.0 else 0 end)/count(*)) from Request r where r.procession.id = ?1")
	Double findRatioPendingRequestsProcession(int processionId);

	//Req 12.3.4 (The ratio of requests to march in a procession, grouped by status: ('APPROVED'))
	@Query("select (sum(case when r.status='APPROVED' then 1.0 else 0 end)/count(*)) from Request r where r.procession.id = ?1")
	Double findRatioAprovedRequestsProcession(int processionId);

	//Req 12.3.4 (The ratio of requests to march in a procession, grouped by status: ('REJECTED'))
	@Query("select (sum(case when r.status='REJECTED' then 1.0 else 0 end)/count(*)) from Request r where r.procession.id = ?1")
	Double findRatioRejectedRequetsProcession(int processionId);

	//Req 12.3.6 (The ratio of requests to march grouped by status: ('PENDING'))
	@Query("select (sum(case when r.status='PENDING' then 1.0 else 0 end)/count(*)) from Request r")
	Double findRatioPendingRequests();

	//Req 12.3.6 (The ratio of requests to march grouped by status: ('APPROVED'))
	@Query("select (sum(case when r.status='APPROVED' then 1.0 else 0 end)/count(*)) from Request r")
	Double findRatioAprovedRequests();

	//Req 12.3.6 (The ratio of requests to march grouped by status: ('REJECTED'))
	@Query("select (sum(case when r.status='REJECTED' then 1.0 else 0 end)/count(*)) from Request r")
	Double findRatioRejectedRequets();

}
