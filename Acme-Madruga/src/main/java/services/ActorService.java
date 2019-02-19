
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
		result.setAddress(actor.getAddress().trim());
		result.setPhoneNumber(this.utilityService.getValidPhone(actor.getPhoneNumber()));

		if (!isUpdating)
			this.boxService.createSystemBoxes(actor);

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

	// Other business methods ---------------------

	protected Actor findPrincipal() {
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

	public void markAsSpammer(final Actor actor) {
		Assert.isTrue(!actor.getIsSpammer());
		actor.setIsSpammer(true);
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

		Collection<Message> messagesSent;
		final List<String> spamWords = new ArrayList<>(this.customisationService.find().getSpamWords());
		String subject, body, tags = "";
		Double counter = 0.;
		Double division = 0.;
		Integer numberMessagesSent;

		messagesSent = this.messageService.findMessagesSentByActor(actor.getId());
		numberMessagesSent = messagesSent.size();

		for (final Message m : messagesSent) {
			subject = m.getSubject();
			body = m.getBody();
			tags = m.getTags();

			System.out.println("subject:" + subject);
			System.out.println("body:" + body);
			System.out.println("tags:" + tags);
			System.out.println("spamwords: " + spamWords);

			for (final String spamWord : spamWords)
				if (subject.contains(spamWord) || body.contains(spamWord) || tags.contains(spamWord)) {
					counter++;
					break;
				}

		}
		division = (counter / (numberMessagesSent * 1.0));
		if (division >= 0.1)
			this.markAsSpammer(actor);

	}
}
