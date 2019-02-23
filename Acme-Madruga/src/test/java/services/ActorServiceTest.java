
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Actor;
import domain.Message;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ActorServiceTest extends AbstractTest {

	// Service under test ---------------------------------

	@Autowired
	private ActorService			actorService;

	@Autowired
	private CustomisationService	customisationService;

	@Autowired
	private MessageService			messageService;


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

		this.actorService.markAsSpammer(actor, true);

		Assert.isTrue(actor.getIsSpammer());

		super.unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIsSpammerError() {
		super.authenticate("admin1");
		Actor actor;

		actor = this.actorService.findOne(super.getEntityId("member1"));

		this.actorService.markAsSpammer(actor, false);

		Assert.isTrue(actor.getIsSpammer());

		super.unauthenticate();
	}

	@Test
	public void testSpammerProcess() {
		super.authenticate("admin1");

		Actor actor;

		actor = this.actorService.findOne(super.getEntityId("brotherhood1"));

		this.actorService.launchSpammerProcess(actor);

	}

	@Test
	public void launchSpammerProcess() {
		super.authenticate("admin1");
		final Actor actor = this.actorService.findOne(super.getEntityId("brotherhood2"));

		Collection<Message> messagesSent;
		final List<String> spamWords = new ArrayList<>(this.customisationService.find().getSpamWords());
		String subject, body, tags = "";
		Double counter = 0.;
		Integer numberMessagesSent;

		messagesSent = this.messageService.findMessagesSentByActor(actor.getId());
		numberMessagesSent = messagesSent.size();
		System.out.println(numberMessagesSent);

		for (final Message m : messagesSent) {
			subject = m.getSubject().toLowerCase();
			body = m.getBody().toLowerCase();
			tags = m.getTags().toLowerCase();
			System.out.println("Subject: " + subject);
			System.out.println("Body:" + body);
			System.out.println("Tags: " + tags);

			for (final String spamWord : spamWords)
				if (subject.contains(spamWord) || body.contains(spamWord) || tags.contains(spamWord)) {
					counter++;
					break;
				}

		}
		System.out.println("Counter final: " + counter);
		System.out.println((counter / (numberMessagesSent * 1.0)));
		if ((counter / (numberMessagesSent * 1.0)) >= 0.1)
			this.actorService.markAsSpammer(actor, true);
		else
			this.actorService.markAsSpammer(actor, false);

		Assert.isTrue(actor.getIsSpammer() == false);

		super.unauthenticate();
	}

	@Test
	public void markAsSpammer() {
		super.authenticate("admin1");

		final Actor actor = this.actorService.findOne(super.getEntityId("brotherhood1"));
		System.out.println(actor);
		Assert.isTrue(actor.getIsSpammer() == null);
		this.actorService.markAsSpammer(actor, true);
		Assert.isTrue(actor.getIsSpammer());

		super.unauthenticate();
	}

	@Test
	public void launchScoreProcess() {
		super.authenticate("admin1");

		final Actor actor = this.actorService.findOne(super.getEntityId("brotherhood1"));
		final Double score;
		final Integer p, n;
		final Double maximo;
		Collection<Message> messagesSent;
		List<Integer> ls;

		messagesSent = this.messageService.findMessagesSentByActor(actor.getId());
		ls = new ArrayList<>(this.positiveNegativeWordNumbers(messagesSent));
		p = ls.get(0);
		n = ls.get(1);

		maximo = this.max(p, n);

		if (maximo != 0)
			score = (p - n) / maximo;
		else
			score = 0.0;

		Assert.isTrue(score >= -1.00 && score <= 1.00);
		System.out.println(score);
		actor.setScore(score);

		super.unauthenticate();
	}

	private List<Integer> positiveNegativeWordNumbers(final Collection<Message> messagesSent) {
		Assert.isTrue(messagesSent != null);

		final List<Integer> results = new ArrayList<Integer>();
		String subject, body, tags = "";
		Integer positive = 0, negative = 0;

		final List<String> positive_ls = new ArrayList<>(this.customisationService.find().getPositiveWords());
		final List<String> negative_ls = new ArrayList<>(this.customisationService.find().getNegativeWords());

		for (final Message m : messagesSent) {
			subject = m.getSubject().toLowerCase();
			body = m.getBody().toLowerCase();
			tags = m.getTags().toLowerCase();

			for (final String pw : positive_ls)
				if (subject.contains(pw) || body.contains(pw) || tags.contains(pw))
					positive++;
			for (final String nw : negative_ls)
				if (subject.contains(nw) || body.contains(nw) || tags.contains(nw))
					negative++;

		}

		results.add(positive);
		results.add(negative);

		return results;

	}

	private Double max(final Integer n, final Integer p) {
		Double result;

		if (n >= p)
			result = n * 1.0;
		else
			result = p * 1.0;

		return result;
	}

}
