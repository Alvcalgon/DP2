
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MessageRepository;
import domain.Actor;
import domain.Box;
import domain.Customisation;
import domain.Message;
import forms.MessageForm;

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

	public Message findOneToMove(final int messageId, final int originBoxId) {
		Message result;
		Box origin;

		result = this.messageRepository.findOne(messageId);
		origin = this.boxService.findOne(originBoxId);

		Assert.notNull(result);
		this.checkSenderOrRecipient(result);
		this.boxService.checkByPrincipal(origin);

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

	public Message createBroadcast() {
		Message result;
		Actor principal;
		Collection<Actor> recipients;

		principal = this.actorService.findPrincipal();
		recipients = this.actorService.findAll();
		recipients.remove(principal);

		result = this.create();
		result.setRecipients(recipients);

		return result;
	}

	public Message send(final Message message) {
		Assert.notNull(message);
		Assert.isTrue(message.getId() == 0);
		this.checkByPrincipal(message);
		this.checkPriority(message);

		Message result;
		Box inBoxRecipient, outBoxSender, spamBoxRecipient;
		boolean isSpam;

		result = this.messageRepository.save(message);

		outBoxSender = this.boxService.findOutBoxFromActor(result.getSender().getId());

		this.boxService.addMessage(outBoxSender, result);

		isSpam = this.messageIsSpam(result);

		if (isSpam) {
			result.setIsSpam(true);
			for (final Actor recipient : result.getRecipients()) {
				spamBoxRecipient = this.boxService.findSpamBoxFromActor(recipient.getId());

				this.boxService.addMessage(spamBoxRecipient, result);
			}
		} else
			for (final Actor recipient : result.getRecipients()) {
				inBoxRecipient = this.boxService.findInBoxFromActor(recipient.getId());

				this.boxService.addMessage(inBoxRecipient, result);
			}

		return result;
	}

	public void delete(final Message message, final Box box) {
		Assert.notNull(message);
		Assert.notNull(box);
		Assert.isTrue(box.getId() != 0 && this.messageRepository.exists(message.getId()));
		Assert.isTrue(box.getMessages().contains(message));
		this.checkSenderOrRecipient(message);
		this.boxService.checkByPrincipal(box);

		Actor principal;
		final Box trashBox;
		List<Box> boxes;
		Integer numberBoxesWithMessage;

		principal = this.actorService.findPrincipal();
		trashBox = this.boxService.findTrashBoxFromActor(principal.getId());

		if (trashBox.equals(box)) {
			boxes = new ArrayList<Box>(this.boxService.findBoxesFromActorThatContaintsAMessage(principal.getId(), message.getId()));
			for (final Box b : boxes)
				this.boxService.removeMessage(b, message);

			this.boxService.removeMessage(trashBox, message);

		} else {
			this.boxService.removeMessage(box, message);
			this.boxService.addMessage(trashBox, message);
		}

		numberBoxesWithMessage = this.boxService.numberOfBoxesThatContaintAMessage(message.getId());
		if (numberBoxesWithMessage == 0)
			this.messageRepository.delete(message);
	}

	// Other business methods -------------------------------
	public void moveMessage(final Message message, final Box origin, final Box destination) {
		Assert.notNull(message);
		Assert.notNull(origin);
		Assert.notNull(destination);
		Assert.isTrue(origin.getId() != 0 && destination.getId() != 0 && this.messageRepository.exists(message.getId()));
		Assert.isTrue(origin.getMessages().contains(message) && !destination.getMessages().contains(message));
		this.checkSenderOrRecipient(message);
		this.boxService.checkByPrincipal(origin);
		this.boxService.checkByPrincipal(destination);

		this.boxService.addMessage(destination, message);
		this.boxService.removeMessage(origin, message);
	}

	public Integer validateDestinationBox(final MessageForm messageForm, final String language, final BindingResult binding) {
		Integer result;

		result = messageForm.getDestinationBoxId();

		if (result == null || result == 0)
			if (language.equals("en"))
				binding.rejectValue("destinationBoxId", "message.error.null", "Must not be null");
			else
				binding.rejectValue("destinationBoxId", "message.error.null", "No debe ser nulo");

		return result;
	}

	public Message sendBroadcast(final Message message) {
		Assert.notNull(message);
		Assert.isTrue(message.getId() == 0);
		this.checkByPrincipal(message);

		Message result;
		boolean isSpam;
		Actor principal;
		Collection<Actor> recipients;
		Box outBoxSender, notificationBoxRecipient, spamBoxRecipient;

		principal = this.actorService.findPrincipal();

		recipients = this.actorService.findAll();
		recipients.remove(principal);

		message.setRecipients(recipients);

		result = this.messageRepository.save(message);

		outBoxSender = this.boxService.findOutBoxFromActor(result.getSender().getId());

		this.boxService.addMessage(outBoxSender, result);

		recipients = result.getRecipients();
		isSpam = this.messageIsSpam(result);
		if (isSpam) {
			result.setIsSpam(true);
			for (final Actor a : recipients) {
				spamBoxRecipient = this.boxService.findSpamBoxFromActor(a.getId());

				this.boxService.addMessage(spamBoxRecipient, result);
			}
		} else
			for (final Actor a : recipients) {
				notificationBoxRecipient = this.boxService.findNotificationBoxFromActor(a.getId());

				this.boxService.addMessage(notificationBoxRecipient, result);
			}

		return result;
	}


	@Autowired
	private Validator	validator;


	public Message reconstruct(final Message message, final BindingResult binding) {
		Message result;
		Collection<Actor> recipients;

		result = this.create();
		result.setSubject(message.getSubject());
		result.setBody(message.getBody());
		result.setPriority(message.getPriority());
		result.setTags(message.getTags());

		recipients = new ArrayList<>();
		if (message.getRecipients() != null)
			recipients.addAll(message.getRecipients());

		result.setRecipients(recipients);

		this.validator.validate(result, binding);

		return result;
	}

	public Message breachNotification() {
		Message message, result;
		List<Actor> recipients;
		Box notificationBox, outBox;
		Actor principal;
		String subject, body;

		recipients = new ArrayList<Actor>();
		recipients.addAll(this.actorService.findAll());

		subject = "Breach notification / Notificación de brecha de seguridad";
		body = "A breach happened. So, we recommend you that update your password /" + "Se produjo una brecha de seguridad. Le recomendamos que actualice su contraseña.";

		message = this.create();
		message.setSubject(subject);
		message.setBody(body);
		message.setPriority("HIGH");
		message.setRecipients(recipients);

		result = this.messageRepository.save(message);

		principal = this.actorService.findPrincipal();

		outBox = this.boxService.findOutBoxFromActor(principal.getId());
		this.boxService.addMessage(outBox, result);

		for (final Actor a : recipients) {
			notificationBox = this.boxService.findNotificationBoxFromActor(a.getId());

			this.boxService.addMessage(notificationBox, result);
		}

		return result;
	}

	// Protected methods ------------------------------------
	protected Double numberMessagesSentByActor(final int actorId) {
		Double result;

		result = this.messageRepository.numberMessagesSentByActor(actorId);

		return result;
	}

	protected Double numberSpamMessagesSentByActor(final int actorId) {
		Double result;

		result = this.messageRepository.numberSpamMessagesSentByActor(actorId);

		return result;
	}

	protected Collection<Message> findMessagesSentByActor(final int actorId) {
		Collection<Message> result;

		result = this.messageRepository.findMessagesSentByActor(actorId);

		return result;
	}

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
		String spam_words;
		List<String> spamWords;
		Customisation customisation;
		String text;
		boolean result;

		customisation = this.customisationService.find();

		spam_words = customisation.getSpamWords();
		spamWords = this.utilityService.ListByString(spam_words);

		text = message.getSubject() + " " + message.getBody();

		result = false;
		for (final String s : spamWords)
			if (text.toLowerCase().contains(s.toLowerCase())) {
				result = true;
				break;
			}

		return result;
	}

	private void checkPriority(final Message message) {
		Customisation customisation;

		customisation = this.customisationService.find();

		Assert.isTrue(customisation.getPriorities().contains(message.getPriority()));
	}
}
