
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Administrator;
import domain.Message;

@Service
@Transactional
public class ActorService {

	// Managed repository --------------------------

	@Autowired
	private ActorRepository			actorRepository;

	// Other supporting services -------------------

	@Autowired
	private UtilityService			utilityService;

	@Autowired
	private BoxService				boxService;

	@Autowired
	private CustomisationService	customisationService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private AdministratorService	administratorService;


	// Constructors -------------------------------

	public ActorService() {
		super();
	}

	// Simple CRUD methods ------------------------

	public Actor save(final Actor actor) {
		Assert.notNull(actor);
		this.utilityService.checkEmailActors(actor);

		final Actor result;
		boolean isUpdating;

		isUpdating = this.actorRepository.exists(actor.getId());
		Assert.isTrue(!isUpdating || this.isOwnerAccount(actor));

		result = this.actorRepository.save(actor);
		if (actor.getAddress() != null)
			result.setAddress(actor.getAddress().trim());
		if (actor.getPhoto() != null)
			result.setPhoto(actor.getPhoto().trim());
		result.setPhoneNumber(this.utilityService.getValidPhone(actor.getPhoneNumber()));

		if (!isUpdating)
			this.boxService.createSystemBoxes(result);

		return result;

	}

	public Collection<Actor> findAll() {
		Collection<Actor> result;

		result = this.actorRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Actor findOne(final int actorId) {
		Assert.isTrue(actorId != 0);

		Actor result;

		result = this.actorRepository.findOne(actorId);
		Assert.notNull(result);

		return result;
	}

	public Actor findOneToDisplayEdit(final int actorId) {
		Assert.isTrue(actorId != 0);

		Actor result, principal;

		principal = this.findPrincipal();
		result = this.actorRepository.findOne(actorId);
		Assert.notNull(result);
		Assert.isTrue(principal.getId() == actorId);

		return result;
	}

	// Other business methods ---------------------

	public Actor findPrincipal() {
		Actor result;
		int userAccountId;

		userAccountId = LoginService.getPrincipal().getId();

		result = this.findActorByUserAccount(userAccountId);
		Assert.notNull(result);

		return result;
	}

	private Actor findActorByUserAccount(final int id) {
		Actor result;

		result = this.actorRepository.findActorByUserAccount(id);

		return result;
	}

	private boolean isOwnerAccount(final Actor actor) {
		int principalId;

		principalId = LoginService.getPrincipal().getId();
		return principalId == actor.getUserAccount().getId();
	}

	protected UserAccount createUserAccount(final String role) {
		UserAccount userAccount;
		Authority authority;

		authority = new Authority();
		authority.setAuthority(role);

		userAccount = new UserAccount();
		userAccount.addAuthority(authority);

		return userAccount;
	}

	public void changeBan(final Actor actor) {
		Assert.notNull(actor);

		final UserAccount userAccount;
		boolean isBanned;

		userAccount = actor.getUserAccount();
		isBanned = userAccount.getIsBanned();

		userAccount.setIsBanned(!isBanned);
	}

	public void markAsSpammer(final Actor actor, final Boolean bool) {
		Assert.notNull(actor);
		actor.setIsSpammer(bool);
	}

	public void spammerProcess() {
		Collection<Actor> all;

		all = this.findAll();

		for (final Actor a : all)
			this.launchSpammerProcess(a);
	}

	protected void launchSpammerProcess(final Actor actor) {
		Assert.notNull(actor);
		Assert.isTrue(actor.getId() != 0);

		Double messagesSent, numberSpamMessages;

		messagesSent = this.messageService.numberMessagesSentByActor(actor.getId());
		numberSpamMessages = this.messageService.numberSpamMessagesSentByActor(actor.getId());

		if ((numberSpamMessages / (messagesSent)) >= 0.1)
			this.markAsSpammer(actor, true);
		else
			this.markAsSpammer(actor, false);

	}

	public void scoreProcess() {
		Collection<Actor> all;
		Administrator system;

		all = this.actorRepository.findSenders();
		system = this.administratorService.findSystem();
		all.remove(system);

		for (final Actor a : all)
			this.launchScoreProcess(a);
	}

	protected void launchScoreProcess(final Actor actor) {
		Assert.notNull(actor);
		Assert.isTrue(actor.getId() != 0);

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

		actor.setScore(Math.round(score * 100d) / 100d);
	}
	private List<Integer> positiveNegativeWordNumbers(final Collection<Message> messagesSent) {
		Assert.isTrue(messagesSent != null);

		final List<Integer> results = new ArrayList<Integer>();
		String body, positiveWords_str, negativeWords_str;
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

	private Double max(final Integer n, final Integer p) {
		Double result;

		if (n >= p)
			result = n * 1.0;
		else
			result = p * 1.0;

		return result;
	}

	public boolean existEmail(final String email) {
		boolean result;
		Actor actor;

		actor = this.actorRepository.findActorByEmail(email);
		result = !(actor == null);

		return result;
	}
}
