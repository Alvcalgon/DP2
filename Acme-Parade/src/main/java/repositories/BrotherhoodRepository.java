
package repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Brotherhood;

@Repository
public interface BrotherhoodRepository extends JpaRepository<Brotherhood, Integer> {

	@Query("select b from Brotherhood b where b.userAccount.id=?1")
	Brotherhood findByUserAccount(int userAccountId);

	@Query("select distinct f.brotherhood from Parade p join p.floats f where p.id= ?1")
	Brotherhood findBrotherhoodByParade(int paradeId);

	@Query("select distinct f.brotherhood from Parade p join p.segments seg join p.floats f where seg.id=?1")
	Brotherhood findBrotherhoodBySegment(int segmentId);

	@Query("select e.brotherhood from Enrolment e where e.member.id = ?1 and e.dropOutMoment=null and e.registeredMoment is not null")
	Collection<Brotherhood> findByMemberId(int memberId);

	@Query("select e.brotherhood from Enrolment e where e.dropOutMoment is null and e.registeredMoment is not null group by e.brotherhood having count(e) = (select max(1.0 * (select count(ee) from Enrolment ee where ee.dropOutMoment is null and ee.registeredMoment is not null and ee.brotherhood.id = b.id group by ee.brotherhood)) from Brotherhood b)")
	Collection<Brotherhood> findLargest();

	@Query("select e.brotherhood from Enrolment e where e.dropOutMoment is null and e.registeredMoment is not null group by e.brotherhood having count(e) = (select min(1.0 * (select count(ee) from Enrolment ee where ee.dropOutMoment is null and ee.registeredMoment is not null and ee.brotherhood.id = b.id group by ee.brotherhood)) from Brotherhood b)")
	Collection<Brotherhood> findSmallest();

	@Query("select b from Brotherhood b where b.area.id=?1")
	Collection<Brotherhood> findBrotherhoodFromArea(int areaId);

	//Req 22.2.1 (a) (The minimum, the maximum, the average, and the standard deviation of the number of brotherhoods per area.)
	@Query("select avg(1.0 * (select count(b) from Brotherhood b where b.area.id = a.id)), min(1.0 * (select count(b) from Brotherhood b where b.area.id = a.id)), max(1.0 * (select count(b) from Brotherhood b where b.area.id = a.id)), stddev(1.0 * (select count(b) from Brotherhood b where b.area.id = a.id)) from Area a)")
	Double[] findDataNumberBrotherhoodPerArea();

	//Req 22.2.1 (b) (The count of the number of brotherhoods per area.)
	@Query("select a.name,(select count(b) from Brotherhood b where b.area.id = a.id) from Area a)")
	List<Object[]> countBrotherhoodPerArea();

	//Req 22.2.1 (c) (The ratio of the number of brotherhoods per area.)
	@Query("select b.area.name, (count(b)/(select count(bb) from Brotherhood bb)*1.0) from Brotherhood b group by b.area")
	List<Object[]> ratioBrotherhoodPerArea();
}
