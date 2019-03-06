
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

	@Autowired
	private UtilityService			utilityService;


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
	public void launchScoreProcess() {
		super.authenticate("admin1");

		final Actor actor = this.actorService.findOne(super.getEntityId("brotherhood1"));
		final Double score;
		final Integer p, n;
		final Double maximo;
		Collection<Message> messagesSent;
		final List<Integer> ls;

		messagesSent = this.messageService.findMessagesSentByActor(actor.getId()); //2
		System.out.println("mensajes enviados" + messagesSent);
		ls = new ArrayList<>(this.positiveNegativeWordNumbers(messagesSent));
		p = ls.get(0); //2
		System.out.println("p" + p);
		n = ls.get(1); // 1
		System.out.println("n" + n);

		maximo = this.max(p, n); //2

		if (maximo != 0)
			score = (p - n) / maximo; // 0.5
		else
			score = 0.0;

		Assert.isTrue(score >= -1.00 && score <= 1.00);
		System.out.println(score);
		actor.setScore(score);

		super.unauthenticate();
	}

	@Test
	public void positiveNegativeWordNumbers() {
		final Actor actor = this.actorService.findOne(super.getEntityId("brotherhood1"));
		Collection<Message> messagesSent;
		messagesSent = this.messageService.findMessagesSentByActor(actor.getId()); //2
		System.out.println("mensajes enviados:" + messagesSent.size());
		final List<Integer> results = new ArrayList<Integer>();
		String body;
		String[] words = {};
		Integer positive = 0, negative = 0;

		final String pos = this.customisationService.find().getPositiveWords();
		final String neg = this.customisationService.find().getNegativeWords();

		final List<String> positive_ls = new ArrayList<>(this.utilityService.ListByString(pos));
		final List<String> negative_ls = new ArrayList<>(this.utilityService.ListByString(neg));

		for (final Message m : messagesSent) {
			body = m.getBody().toLowerCase();
			words = body.split(" ");
			System.out.println("cuerpo:" + words);

			for (final String word : words)
				if (positive_ls.contains(word))
					positive++;
				else if (negative_ls.contains(word))
					negative++;
			System.out.println("Palabras positivas: " + positive);
			System.out.println("Palabras negativas: " + negative);
		}

		results.add(positive); //2
		results.add(negative);	// 1
		System.out.println("results: " + results);

	}
	private Double max(final Integer n, final Integer p) {
		Double result;

		if (n >= p)
			result = n * 1.0;
		else
			result = p * 1.0;

		return result;
	}

	private List<Integer> positiveNegativeWordNumbers(final Collection<Message> messagesSent) {
		Assert.isTrue(messagesSent != null);

		final List<Integer> results = new ArrayList<Integer>();
		String body;
		String positiveWords_str, negativeWords_str;
		Integer positive = 0, negative = 0;
		String[] words = {};

		positiveWords_str = this.customisationService.find().getPositiveWords();
		negativeWords_str = this.customisationService.find().getNegativeWords();

		final List<String> positive_ls = new ArrayList<>(this.utilityService.ListByString(positiveWords_str));
		final List<String> negative_ls = new ArrayList<>(this.utilityService.ListByString(negativeWords_str));

		for (final Message m : messagesSent) {
			body = m.getBody().toLowerCase();
			words = body.split(" ");

			for (final String word : words)
				if (positive_ls.contains(word))
					positive++;
				else if (negative_ls.contains(word))
					negative++;
		}

		results.add(positive);
		results.add(negative);

		return results;

	}

}
