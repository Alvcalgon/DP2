
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Finder;

@Repository
public interface FinderRepository extends JpaRepository<Finder, Integer> {

	@Query("select count(f)/(select count(ff) from Finder ff where ff.keyword is not null or ff.area is not null or ff.minimumDate is not null or ff.maximumDate is not null)*1.0 from Finder f where f.keyword is null and f.area is null and f.minimumDate is null and f.maximumDate is null")
	Double findRatioEmptyVsNonEmpty();

	@Query("select f from Finder f join f.processions p where p.id =?1")
	Collection<Finder> findFinderByProcession(int id);

	@Query("select f from Finder f where f.member.id = ?1")
	Finder findByMemberId(int memberId);

}
