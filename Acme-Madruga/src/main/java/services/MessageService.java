
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

import repositories.MessageRepository;
import domain.Actor;
import domain.Box;
import domain.Customisation;
import domain.Enrolment;
import domain.Message;
import domain.Procession;
import domain.Request;

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

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private BrotherhoodService		brotherhoodService;


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

	// Situacion: Box1 contiene el mensaje m1 y Box2 tambien contiene a m1.
	// Box1 y Box2 son 2 carpetas personalizables. Borramos m1 de Box1, entonces
	// la trash box del actor pasa a tener m1. Luego, borramos m1 de Box2,
	// entonces, trash box tendra m1 duplicado??
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
	public void copyMessage(final Message message, final Box destination) {
		Assert.notNull(message);
		Assert.notNull(destination);
		Assert.isTrue(destination.getId() != 0 && this.messageRepository.exists(message.getId()));
		Assert.isTrue(!destination.getMessages().contains(message));
		this.boxService.checkByPrincipal(destination);

		this.boxService.addMessage(destination, message);
	}

	public void moveMessage(final Message message, final Box origin, final Box destination) {
		Assert.notNull(message);
		Assert.notNull(origin);
		Assert.notNull(destination);
		Assert.isTrue(origin.getId() != 0 && destination.getId() != 0 && this.messageRepository.exists(message.getId()));
		Assert.isTrue(origin.getMessages().contains(message) && !destination.getMessages().contains(message));
		this.boxService.checkByPrincipal(origin);
		this.boxService.checkByPrincipal(destination);

		this.boxService.addMessage(destination, message);
		this.boxService.removeMessage(origin, message);
	}

	public Message sendBroadcast(final Message message) {
		Assert.notNull(message);
		Assert.isTrue(message.getId() == 0);
		this.checkByPrincipal(message);

		Message result;
		boolean isSpam;
		Collection<Actor> allActors;
		Box outBoxSender, notificationBoxRecipient, spamBoxRecipient;

		allActors = this.actorService.findAll();

		message.setRecipients(allActors);

		result = this.messageRepository.save(message);

		outBoxSender = this.boxService.findOutBoxFromActor(result.getSender().getId());

		this.boxService.addMessage(outBoxSender, result);

		isSpam = this.messageIsSpam(result);
		if (isSpam)
			for (final Actor a : allActors) {
				spamBoxRecipient = this.boxService.findSpamBoxFromActor(a.getId());

				this.boxService.addMessage(spamBoxRecipient, result);
			}
		else
			for (final Actor a : allActors) {
				notificationBoxRecipient = this.boxService.findNotificationBoxFromActor(a.getId());

				this.boxService.addMessage(notificationBoxRecipient, result);
			}

		return result;
	}

	public void notificationChangeStatus(final Request request) {
		//		Assert.notNull(request);
		//		Assert.isTrue(request.getId() != 0);
		//		
		//		Message message, result;
		//		Actor system, member, brotherhood;
		//		Box outBoxSystem, notificationBoxRecipient;
		//		List<Actor> recipients;
		//		String subject, body;
		//		
		//		// system = this.administratorService.findSystem();
		//		member = request.getMember();
		//		//brotherhood = this.brotherhoodService.findBrotherhoodByProcession(request.getProcession().getId());
		//		
		//		recipients = new ArrayList<Actor>();
		//		recipients.add(member);
		//		// recipients.add(brotherhood);
		//		
		//		subject = "request notification / Notificación de solicitud.";
		//		body = "The status of the request has changed / El estado de la solicitud ha cambiado: " + request.getStatus();
		//		
		//		message = this.createNotification(system, recipients, subject, body);
		//		result = this.messageRepository.save(message);
		//			
		//		outBoxSystem = this.boxService.findOutBoxFromActor(system.getId());
		//		this.boxService.addMessage(outBoxSystem, result);
		//		
		//		for (Actor a: recipients) {
		//			notificationBoxRecipient = this.boxService.findNotificationBoxFromActor(a.getId());
		//			
		//			this.boxService.addMessage(notificationBoxRecipient, result);
		//		}
		//		
		//		return result;
	}

	public void notificationEnrolment(final Enrolment enrolment) {

	}

	public void notificationDropOut(final Enrolment enrolment) {

	}

	public void notificationPublishedProcession(final Procession procession) {

	}

	// Protected methods ------------------------------------
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
				break;
			}

		return result;
	}

	private void checkPriority(final Message message) {
		Customisation customisation;

		customisation = this.customisationService.find();

		Assert.isTrue(customisation.getPriorities().contains(message.getPriority()));
	}

	private Message createNotification(final Actor sender, final Collection<Actor> recipients, final String subject, final String body) {
		Message result;
		Date current_moment;

		current_moment = this.utilityService.current_moment();

		result = new Message();
		result.setSender(sender);
		result.setRecipients(recipients);
		result.setSentMoment(current_moment);
		result.setPriority("NEUTRAL");
		result.setBody(body);
		result.setSubject(subject);

		return result;
	}

}
