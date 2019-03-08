
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {

	@Query("select a from Actor a where a.userAccount.id=?1")
	Actor findActorByUserAccount(int userAccountId);

	@Query("select a from Actor a where a.email=?1")
	Actor findActorByEmail(String email);

	@Query("select distinct m.sender from Message m")
	Collection<Actor> findSenders();

}
