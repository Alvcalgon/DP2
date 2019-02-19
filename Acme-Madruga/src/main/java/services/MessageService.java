
package services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.MessageRepository;
import domain.Actor;
import domain.Box;
import domain.Customisation;
import domain.Message;

@Service
@Transactional
public class MessageService {

	// Managed repository  ----------------------------------
	@Autowired
	private MessageRepository		messageRepository;

	// Supporting services ----------------------------------
	@Autowired
	private BoxService				boxService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private UtilityService			utilityService;

	@Autowired
	private CustomisationService	customisationService;


	// Constructors -----------------------------------------
	public MessageService() {
		super();
	}

	// Simple CRUD methods ----------------------------------
	public Message findOne(final int messageId) {
		Message result;

		result = this.messageRepository.findOne(messageId);

		return result;
	}

	public Message findOneToDisplay(final int messageId) {
		Message result;

		result = this.messageRepository.findOne(messageId);

		Assert.notNull(result);
		this.checkSenderOrRecipient(result);

		return result;
	}

	public Message create() {
		Message result;
		Actor principal;
		Date current_moment;

		principal = this.actorService.findPrincipal();
		current_moment = this.utilityService.current_moment();

		result = new Message();
		result.setSender(principal);
		result.setRecipients(Collections.<Actor> emptySet());
		result.setSentMoment(current_moment);

		return result;
	}

	public Message send(final Message message) {
		Assert.notNull(message);
		Assert.isTrue(message.getId() == 0);
		this.checkByPrincipal(message);

		Message result;
		Box inBoxRecipient, outBoxSender;
		boolean isSpam;

		result = this.messageRepository.save(message);

		outBoxSender = this.boxService.findOutBoxFromActor(message.getSender().getId());

		this.boxService.addMessage(outBoxSender, message);

		isSpam = this.messageIsSpam(result);

		if (isSpam) {
			result.setIsSpam(true);
			for (final Actor recipient : message.getRecipients()) {
				inBoxRecipient = this.boxService.findSpamBoxFromActor(recipient.getId());

				this.boxService.addMessage(inBoxRecipient, result);
			}
		} else
			for (final Actor recipient : message.getRecipients()) {
				inBoxRecipient = this.boxService.findInBoxFromActor(recipient.getId());

				this.boxService.addMessage(inBoxRecipient, result);
			}

		return result;
	}

	// Other business methods -------------------------------

	// Protected methods ------------------------------------

	// Private methods --------------------------------------
	private void checkByPrincipal(final Message message) {
		Actor principal;

		principal = this.actorService.findPrincipal();

		Assert.notNull(principal);
		Assert.isTrue(message.getSender().equals(principal));
	}

	private void checkSenderOrRecipient(final Message message) {
		Actor principal;

		principal = this.actorService.findPrincipal();

		Assert.isTrue(message.getSender().equals(principal) || message.getRecipients().contains(principal));
	}

	private boolean messageIsSpam(final Message message) {
		List<String> spamWords;
		Customisation customisation;
		String text;
		boolean result;

		customisation = this.customisationService.find();
		spamWords = new ArrayList<String>(customisation.getSpamWords());
		text = message.getSubject() + " " + message.getBody();

		result = false;
		for (final String spam : spamWords)
			if (text.toLowerCase().contains(spam.toLowerCase())) {
				result = true;
				;
				break;
			}

		return result;
	}

}
