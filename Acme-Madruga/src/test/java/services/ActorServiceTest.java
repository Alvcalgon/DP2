
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Actor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ActorServiceTest extends AbstractTest {

	// Service under test ---------------------------------

	@Autowired
	private ActorService	actorService;


	// Tests ----------------------------------------------

	@Test
	public void testFindAll() {
		Collection<Actor> actors;
		actors = this.actorService.findAll();
		Assert.notEmpty(actors);
		Assert.notNull(actors);
	}

	@Test
	public void testFindOne() {
		Actor actor;
		actor = this.actorService.findOne(super.getEntityId("administrator1"));
		Assert.notNull(actor);
	}

	@Test
	public void testUpdateActor() {
		Actor actor, saved;
		String middleName, newMiddleName;

		super.authenticate("admin1");

		actor = this.actorService.findOne(super.getEntityId("administrator1"));
		newMiddleName = "manuel";

		middleName = actor.getMiddleName();

		actor.setMiddleName(newMiddleName);

		saved = this.actorService.save(actor);

		Assert.isTrue(!saved.getMiddleName().equals(middleName));

		Assert.notNull(this.actorService.findOne(saved.getId()));

		super.authenticate(null);

	}

	@Test
	public void testIsBanner() {
		super.authenticate("admin1");
		Actor actor;
		actor = this.actorService.findOne(super.getEntityId("member1"));
		Assert.isTrue(!(actor.getUserAccount().getIsBanned()));
		this.actorService.changeBan(actor);
		Assert.isTrue(actor.getUserAccount().getIsBanned());
		super.unauthenticate();
	}

	@Test
	public void testIsSpammer() {
		super.authenticate("admin1");
		Actor actor;

		actor = this.actorService.findOne(super.getEntityId("member1"));

		this.actorService.markAsSpammer(actor);

		Assert.isTrue(actor.getIsSpammer());

		super.unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIsSpammerError() {
		super.authenticate("admin1");
		Actor actor;

		actor = this.actorService.findOne(super.getEntityId("member1"));

		this.actorService.markAsSpammer(actor);

		Assert.isTrue(actor.getIsSpammer());

		super.unauthenticate();
	}

	@Test
	public void testSpammerProcess() {
		super.authenticate("admin1");

		Actor actor;

		actor = this.actorService.findOne(super.getEntityId("brotherhood1"));

		this.actorService.launchSpammerProcess(actor);

		System.out.println(actor.getIsSpammer());
	}

}
