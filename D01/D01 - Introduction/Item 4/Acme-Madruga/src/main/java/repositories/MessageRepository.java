
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

	@Query("select count(m)*1.0 from Message m where m.sender.id = ?1")
	Double numberMessagesSentByActor(int actorId);

	@Query("select count(m)*1.0 from Message m where m.sender.id = ?1 and m.isSpam = true")
	Double numberSpamMessagesSentByActor(int actorId);

	@Query("select m from Message m where m.sender.id = ?1")
	Collection<Message> findMessagesSentByActor(int actorId);

}
