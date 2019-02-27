
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

import repositories.MessageRepository;
import domain.Actor;
import domain.Box;
import domain.Customisation;
import domain.Enrolment;
import domain.Message;
import domain.Procession;
import domain.Request;
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

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private MemberService			memberService;


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

	public Integer validateDestinationBox(final MessageForm messageForm, final BindingResult binding) {
		Integer result;

		result = messageForm.getDestinationBoxId();

		if (result == null || result == 0)
			binding.rejectValue("destinationBoxId", "message.error.null", "Must not be null");

		return result;
	}

	public Message sendBroadcast(final Message message) {
		Assert.notNull(message);
		Assert.isTrue(message.getId() == 0);
		this.checkByPrincipal(message);

		Message result;
		boolean isSpam;
		Collection<Actor> recipients;
		Box outBoxSender, notificationBoxRecipient, spamBoxRecipient;

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

	public Message notificationChangeStatus(final Request request) {
		Assert.notNull(request);
		Assert.isTrue(request.getId() != 0);

		Message message, result;
		Actor system, member, brotherhood;
		Box outBoxSystem, notificationBoxRecipient;
		List<Actor> recipients;
		String subject, body, fullname_brotherhood, fullname_member;

		system = this.administratorService.findSystem();
		member = request.getMember();
		brotherhood = this.brotherhoodService.findBrotherhoodByProcession(request.getProcession().getId());

		recipients = new ArrayList<Actor>();
		recipients.add(member);
		recipients.add(brotherhood);

		fullname_brotherhood = brotherhood.getFullname();
		fullname_member = member.getFullname();

		subject = "request notification / Notificación de solicitud.";
		body = "The status of the request related with the brotherhood " + fullname_brotherhood + " and the member " + fullname_member + " has changed / El estado de la solicitud relacionada con la hermandad " + fullname_brotherhood + " y el miembro "
			+ fullname_member + " ha cambiado: " + request.getStatus();

		message = this.createNotification(system, recipients, subject, body);
		result = this.messageRepository.save(message);

		outBoxSystem = this.boxService.findOutBoxFromActor(system.getId());
		this.boxService.addMessage(outBoxSystem, result);

		for (final Actor a : recipients) {
			notificationBoxRecipient = this.boxService.findNotificationBoxFromActor(a.getId());

			this.boxService.addMessage(notificationBoxRecipient, result);
		}

		return result;
	}

	public Message notificationEnrolment(final Enrolment enrolment) {
		Assert.notNull(enrolment);
		Assert.isTrue(enrolment.getId() != 0);

		Message message, result;
		Box outBoxSystem, notificationBoxRecipient;
		Actor member, brotherhood, system;
		List<Actor> recipients;
		String subject, body, fullname_brotherhood, fullname_member;

		system = this.administratorService.findSystem();
		brotherhood = enrolment.getBrotherhood();
		member = enrolment.getMember();

		recipients = new ArrayList<Actor>();
		recipients.add(brotherhood);
		recipients.add(member);

		fullname_member = member.getFullname();
		fullname_brotherhood = brotherhood.getFullname();

		subject = "A enrolment notification / Una notificación de inscripción";
		body = "The member " + fullname_member + " has joined to the brotherhood " + fullname_brotherhood + ". / El miembro " + fullname_member + " se ha unido a la hermandad " + fullname_brotherhood;

		message = this.createNotification(system, recipients, subject, body);
		result = this.messageRepository.save(message);

		outBoxSystem = this.boxService.findOutBoxFromActor(system.getId());

		this.boxService.addMessage(outBoxSystem, result);

		for (final Actor a : recipients) {
			notificationBoxRecipient = this.boxService.findNotificationBoxFromActor(a.getId());

			this.boxService.addMessage(notificationBoxRecipient, result);
		}

		return result;
	}

	public Message notificationDropOut(final Enrolment enrolment) {
		Assert.notNull(enrolment);
		Assert.isTrue(enrolment.getId() != 0);

		Message message, result;
		Box outBoxSystem, notificationBoxRecipient;
		Actor member, brotherhood, system;
		List<Actor> recipients;
		String subject, body, fullname_brotherhood, fullname_member;

		system = this.administratorService.findSystem();
		brotherhood = enrolment.getBrotherhood();
		member = enrolment.getMember();

		recipients = new ArrayList<Actor>();
		recipients.add(brotherhood);
		recipients.add(member);

		fullname_member = member.getFullname();
		fullname_brotherhood = brotherhood.getFullname();

		subject = "A drop out notification / Una notificación de abandono";
		body = "The member " + fullname_member + " has dropped out the brotherhood " + fullname_brotherhood + ". / El miembro " + fullname_member + " ha abandonado la hermandad " + fullname_brotherhood;

		message = this.createNotification(system, recipients, subject, body);
		result = this.messageRepository.save(message);

		outBoxSystem = this.boxService.findOutBoxFromActor(system.getId());

		this.boxService.addMessage(outBoxSystem, result);

		for (final Actor a : recipients) {
			notificationBoxRecipient = this.boxService.findNotificationBoxFromActor(a.getId());

			this.boxService.addMessage(notificationBoxRecipient, result);
		}

		return result;
	}

	public Message notificationPublishedProcession(final Procession procession) {
		Assert.notNull(procession);
		Assert.isTrue(procession.getId() != 0 && procession.getIsFinalMode());

		Message message, result;
		Box outBoxSystem, notificationBoxRecipient;
		Actor brotherhood, system;
		List<Actor> recipients;
		String subject, body, ticker;

		system = this.administratorService.findSystem();
		brotherhood = this.brotherhoodService.findBrotherhoodByProcession(procession.getId());

		recipients = new ArrayList<Actor>();
		recipients.add(brotherhood);
		recipients.addAll(this.memberService.findEnroledMemberByBrotherhood(brotherhood.getId()));

		ticker = procession.getTicker();

		subject = "A publication notification / Una notificación de publicación";
		body = "The procession whose ticker is " + ticker + " has been published / La procesión cuyo ticker es " + ticker + " ha sido publicada";

		message = this.createNotification(system, recipients, subject, body);
		result = this.messageRepository.save(message);

		outBoxSystem = this.boxService.findOutBoxFromActor(system.getId());

		this.boxService.addMessage(outBoxSystem, result);

		for (final Actor a : recipients) {
			notificationBoxRecipient = this.boxService.findNotificationBoxFromActor(a.getId());

			this.boxService.addMessage(notificationBoxRecipient, result);
		}

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
