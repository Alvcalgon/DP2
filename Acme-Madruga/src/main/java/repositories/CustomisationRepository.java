
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Customisation;

@Repository
public interface CustomisationRepository extends JpaRepository<Customisation, Integer> {

}
