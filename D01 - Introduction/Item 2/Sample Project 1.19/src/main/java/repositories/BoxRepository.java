
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

	@Query("select b from Box b where b.actor.id=?1 and b.parent is null")
	Collection<Box> findRootBoxesByActor(int actorId);

	@Query("select b from Box b where b.parent.id=?1")
	Collection<Box> findChildBoxesByBox(int boxId);

	@Query("select b from Box b where b.actor.id=?1 and b.name='in box' and b.isSystemBox=true")
	Box findInBoxFromActor(int actorId);

	@Query("select b from Box b where b.actor.id=?1 and b.name='out box' and b.isSystemBox=true")
	Box findOutBoxFromActor(int actorId);

	@Query("select b from Box b where b.actor.id=?1 and b.name='spam box' and b.isSystemBox=true")
	Box findSpamBoxFromActor(int actorId);

	@Query("select b from Box b where b.actor.id=?1 and b.name='notification box' and b.isSystemBox=true")
	Box findNotificationBoxFromActor(int actorId);

	@Query("select b from Box b where b.actor.id=?1 and b.name='trash box' and b.isSystemBox=true")
	Box findTrashBoxFromActor(int actorId);

	@Query("select b from Box b join b.messages m where b.actor.id=?1 and m.id=?2")
	Collection<Box> findBoxesFromActorThatContaintsAMessage(int actorId, int messageId);

	@Query("select count(b) from Box b join b.messages m where m.id=?1")
	Integer numberOfBoxesThatContaintAMessage(int messageId);
}
