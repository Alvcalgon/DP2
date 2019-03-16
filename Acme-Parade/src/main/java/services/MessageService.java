
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
import domain.Parade;
import domain.Request;
import domain.Sponsor;
import domain.Sponsorship;
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
		this.checkByPrincipal(message);
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
		brotherhood = this.brotherhoodService.findBrotherhoodByParade(request.getParade().getId());

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

	public Message notificationFare(final Sponsorship sponsorship) {
		Assert.notNull(sponsorship);
		Assert.isTrue(sponsorship.getId() != 0);

		Message message, result;
		Actor system;
		Sponsor sponsor;
		Box outBoxSystem, notificationBoxRecipient;
		List<Actor> recipients;
		String subject, body;
		Customisation customisation;
		double fare, vat, finalFare;

		system = this.administratorService.findSystem();
		customisation = this.customisationService.find();
		sponsor = sponsorship.getSponsor();
		fare = customisation.getFare();
		vat = customisation.getVatPercentage();
		finalFare = fare * (1.0 + vat);
		recipients = new ArrayList<Actor>();
		recipients.add(sponsor);

		subject = "Fare notification. / Notificación de tarifas.";
		body = "The payment of " + finalFare + "euros has been made in one of you sponsorships for the" + sponsorship.getParade().getTitle() + "parade. / Se ha realizado el cobro de " + finalFare + "euros de uno de sus patrocinios para el desfile "
			+ sponsorship.getParade().getTitle() + ".";

		message = this.createNotification(system, recipients, subject, body);
		result = this.messageRepository.save(message);

		outBoxSystem = this.boxService.findOutBoxFromActor(system.getId());
		this.boxService.addMessage(outBoxSystem, result);

		notificationBoxRecipient = this.boxService.findNotificationBoxFromActor(sponsor.getId());
		this.boxService.addMessage(notificationBoxRecipient, result);

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

	public Message notificationRemove(final Enrolment enrolment) {
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

		subject = "A expulsion notification / Una notificación de expulsión";
		body = "The member " + fullname_member + " has been expelled from the brotherhood " + fullname_brotherhood + ". / El miembro " + fullname_member + " ha sido expulsado de la hermandad " + fullname_brotherhood;

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

	public Message notificationPublishedParade(final Parade parade) {
		Assert.notNull(parade);
		Assert.isTrue(parade.getId() != 0 && parade.getIsFinalMode());

		Message message, result;
		Box outBoxSystem, notificationBoxRecipient;
		Actor brotherhood, system;
		List<Actor> recipients;
		String subject, body, ticker;

		system = this.administratorService.findSystem();
		brotherhood = this.brotherhoodService.findBrotherhoodByParade(parade.getId());

		recipients = new ArrayList<Actor>();
		recipients.add(brotherhood);

		ticker = parade.getTicker();

		subject = "A publication notification / Una notificación de publicación";
		body = "The parade whose ticker is " + ticker + " has been published / La desfile cuyo ticker es " + ticker + " ha sido publicada";

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
