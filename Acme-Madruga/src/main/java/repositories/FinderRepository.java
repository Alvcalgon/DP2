
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Finder;

@Repository
public interface FinderRepository extends JpaRepository<Finder, Integer> {

	@Query("select count(f)/(select count(ff) from Finder ff where ff.keyword is not null or ff.area is not null or ff.minimumDate is not null or ff.maximumDate is not null)*1.0 from Finder f where f.keyword is null and f.area is null and f.minimumDate is null and f.maximumDate is null")
	Double findRatioEmptyVsNonEmpty();

}
