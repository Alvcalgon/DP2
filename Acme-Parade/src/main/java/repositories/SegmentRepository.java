
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Segment;

@Repository
public interface SegmentRepository extends JpaRepository<Segment, Integer> {

	@Query("select s from Parade p join p.segments s where p.id=?1 order by s.reachingOrigin ASC")
	Collection<Segment> findOrderedSegments(int paradeId);

}
