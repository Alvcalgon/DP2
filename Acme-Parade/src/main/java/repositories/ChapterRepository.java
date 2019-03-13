
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Chapter;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Integer> {

	@Query("select c from Chapter c where c.userAccount.id=?1")
	Chapter findByUserAccount(int userAccountId);

	// Req 8.1.3 Chapters coordinate at least 10% more parades than average
	@Query("select (select c from Chapter c where c.area.id = a.id),(select count(p) from Parade p join p.floats f where f.brotherhood.area.id = a.id group by a.name having count(p) >=1.1 * ?1) from Area a where a in (select c.area from Chapter c))")
	List<Object[]> chaptersCoordinateLeast10MoreParadasThanAverage(double avg);

}
