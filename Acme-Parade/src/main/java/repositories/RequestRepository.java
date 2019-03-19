
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

	@Query("select r from Request r where r.member.id =?1 and r.parade.id=?2")
	Collection<Request> findRequestByMemberParade(int id, int paradeId);

	@Query("select r from Request r where r.member.id = ?1")
	Collection<Request> findRequestByMemberId(int id);

	@Query("select r from Request r where r.member.id = ?1 and r.status='PENDING'")
	Collection<Request> findPendingRequestByMember(int id);

	@Query("select r from Request r where r.member.id = ?1 and r.status='APPROVED'")
	Collection<Request> findApprovedRequestByMember(int id);

	@Query("select r from Request r where r.member.id = ?1 and r.status='REJECTED'")
	Collection<Request> findRejectedRequestByMember(int id);

	@Query("select distinct r from Request r join r.parade p join p.floats f where f.brotherhood.id = ?1 and r.status='PENDING'")
	Collection<Request> findPendingRequestByBrotherhood(int id);

	@Query("select distinct r from Request r join r.parade p join p.floats f where f.brotherhood.id = ?1 and r.status='APPROVED'")
	Collection<Request> findApprovedRequestByBrotherhood(int id);

	@Query("select distinct r from Request r join r.parade p join p.floats f where f.brotherhood.id = ?1 and r.status='REJECTED'")
	Collection<Request> findRejectedRequestByBrotherhood(int id);

	@Query("select distinct r from Request r join r.parade.floats f where r.member.id = ?1 and f.brotherhood.id = ?2")
	Collection<Request> findRequestByMemberIdBrotherhoodId(int memberId, int brotherhoodId);

	@Query("select r from Request r where r.parade.id=?1")
	Collection<Request> findRequestByParade(int paradeId);

	//Req 12.3.4 (The ratio of requests to march in a parade, grouped by status: ('PENDING'))
	@Query("select (sum(case when r.status='PENDING' then 1.0 else 0 end)/count(*)) from Request r where r.parade.id = ?1")
	Double findRatioPendingRequestsParade(int paradeId);

	//Req 12.3.4 (The ratio of requests to march in a parade, grouped by status: ('APPROVED'))
	@Query("select (sum(case when r.status='APPROVED' then 1.0 else 0 end)/count(*)) from Request r where r.parade.id = ?1")
	Double findRatioAprovedRequestsParade(int paradeId);

	//Req 12.3.4 (The ratio of requests to march in a parade, grouped by status: ('REJECTED'))
	@Query("select (sum(case when r.status='REJECTED' then 1.0 else 0 end)/count(*)) from Request r where r.parade.id = ?1")
	Double findRatioRejectedRequetsParade(int paradeId);

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
