
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Chapter;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Integer> {

	@Query("select c from Chapter c where c.userAccount.id=?1")
	Chapter findByUserAccount(int userAccountId);

	// Req 8.1.3 Chapters coordinate at least 10% more parades than average
	@Query("select c from Chapter c where (select count(p) from Parade p join p.floats f where f.brotherhood.area.id = c.area.id) > 1.1*(select avg(1.0 * (select count(p) from Parade p join p.floats f where f.brotherhood.area.id = a.id)) from Area a where a in (select c.area from Chapter c))")
	Collection<Chapter> chaptersCoordinateLeast10MoreParadasThanAverage(double avg);

}
