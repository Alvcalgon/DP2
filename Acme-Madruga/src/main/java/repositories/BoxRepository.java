
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Box;

@Repository
public interface BoxRepository extends JpaRepository<Box, Integer> {

	@Query("select b from Box b where b.actor.id=?1")
	Collection<Box> findBoxesByActor(int actorId);

	@Query("select b from Box b where b.parent.id=?1")
	Collection<Box> findChildBoxesByBox(int boxId);

	@Query("select b from Box b where b.actor.id=?1 and b.name='in box'")
	Box findInBoxFromActor(int actorId);

	@Query("select b from Box b where b.actor.id=?1 and b.name='out box'")
	Box findOutBoxFromActor(int actorId);

	@Query("select b from Box b where b.actor.id=?1 and b.name='spam box'")
	Box findSpamBoxFromActor(int actorId);

	@Query("select b from Box b where b.actor.id=?1 and b.name='notification box'")
	Box findNotificationBoxFromActor(int actorId);

	@Query("select b from Box b where b.actor.id=?1 and b.name='trash box'")
	Box findTrashBoxFromActor(int actorId);
}
