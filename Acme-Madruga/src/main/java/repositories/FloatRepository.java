
package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Float;

@Repository
public interface FloatRepository extends JpaRepository<Float, Integer> {

	@Query("select distinct f from Float f where f.brotherhood.id=?1")
	Page<Float> findFloatByBrotherhood(int id, Pageable pageable);

}
