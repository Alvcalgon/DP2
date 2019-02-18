
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.ParadeFloat;

@Repository
public interface ParadeFloatRepository extends JpaRepository<ParadeFloat, Integer> {

}
