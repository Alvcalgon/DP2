
package services;

import java.util.Collection;
import java.util.Collections;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.BoxRepository;
import domain.Actor;
import domain.Box;
import domain.Message;

@Service
@Transactional
public class BoxService {

	// Managed repository --------------------------
	@Autowired
	private BoxRepository	boxRepository;

	// Other supporting services -------------------
	@Autowired
	private ActorService	actorService;


	// Constructors -------------------------------
	public BoxService() {
		super();
	}

	// Simple CRUD methods ------------------------
	public Box findOne(final int boxId) {
		Box result;

		result = this.boxRepository.findOne(boxId);

		return result;
	}

	public Box findOneToEditOrDisplay(final int boxId) {
		Box result;

		result = this.boxRepository.findOne(boxId);

		Assert.notNull(result);
		this.checkByPrincipal(result);

		return result;
	}

	public Box create() {
		Box result;
		final Actor principal;

		principal = this.actorService.findPrincipal();

		result = new Box();
		result.setActor(principal);
		result.setMessages(Collections.<Message> emptySet());

		return result;
	}

	public Box save(final Box box) {
		Assert.notNull(box);
		Assert.isTrue(!box.getIsSystemBox());
		this.checkByPrincipal(box);
		this.checkName(box);

		Box result;

		result = this.boxRepository.save(box);

		return result;
	}

	public void delete(final Box box) {
		Assert.notNull(box);
		Assert.isTrue(!box.getIsSystemBox());
		Assert.isTrue(this.boxRepository.exists(box.getId()));

		Actor principal;
		Box trashBox;
		Collection<Message> messages, childMessages;
		Collection<Box> childBoxes;

		principal = this.actorService.findPrincipal();
		trashBox = this.findTrashBoxFromActor(principal.getId());

		// If this box contains messages, we must move those messages to
		// trash box.
		messages = box.getMessages();

		if (messages != null && !messages.isEmpty())
			trashBox.getMessages().addAll(messages);

		// If this box contains other boxes, we must delete them. If those
		// sub folders contains also some messages, we must to move them
		// trash box.
		childBoxes = this.findChildBoxesByBox(box.getId());

		if (childBoxes != null && !childBoxes.isEmpty()) {
			for (final Box child : childBoxes) {
				childMessages = child.getMessages();

				if (childMessages != null && !childMessages.isEmpty())
					trashBox.getMessages().addAll(childMessages);
			}

			this.boxRepository.delete(childBoxes);
		}

		this.boxRepository.delete(box);
	}

	// Other business methods ---------------------
	protected void createSystemBoxes(final Actor actor) {
		Assert.notNull(actor);

		Box inBox, outBox, notificationBox, trashBox, spamBox;

		inBox = new Box();
		outBox = new Box();
		notificationBox = new Box();
		trashBox = new Box();
		spamBox = new Box();

		inBox.setActor(actor);
		outBox.setActor(actor);
		notificationBox.setActor(actor);
		trashBox.setActor(actor);
		spamBox.setActor(actor);

		inBox.setMessages(Collections.<Message> emptySet());
		outBox.setMessages(Collections.<Message> emptySet());
		notificationBox.setMessages(Collections.<Message> emptySet());
		trashBox.setMessages(Collections.<Message> emptySet());
		spamBox.setMessages(Collections.<Message> emptySet());

		inBox.setIsSystemBox(true);
		outBox.setIsSystemBox(true);
		notificationBox.setIsSystemBox(true);
		trashBox.setIsSystemBox(true);
		spamBox.setIsSystemBox(true);

		inBox.setName("in box");
		outBox.setName("out box");
		notificationBox.setName("notification box");
		trashBox.setName("trash box");
		spamBox.setName("spam box");

		this.boxRepository.save(inBox);
		this.boxRepository.save(outBox);
		this.boxRepository.save(trashBox);
		this.boxRepository.save(notificationBox);
		this.boxRepository.save(spamBox);
	}

	public Collection<Box> findRootBoxesByActor(final int actorId) {
		Collection<Box> results;

		results = this.boxRepository.findRootBoxesByActor(actorId);

		return results;
	}

	public Collection<Box> findChildBoxesByBox(final int boxId) {
		Collection<Box> results;

		results = this.boxRepository.findChildBoxesByBox(boxId);

		return results;
	}

	// Protected methods --------------------------
	protected Collection<Box> findBoxesByActor(final int actorId) {
		Collection<Box> results;

		results = this.boxRepository.findBoxesByActor(actorId);

		return results;
	}

	protected Box findInBoxFromActor(final int actorId) {
		Box result;

		result = this.boxRepository.findInBoxFromActor(actorId);

		return result;
	}

	protected Box findOutBoxFromActor(final int actorId) {
		Box result;

		result = this.boxRepository.findOutBoxFromActor(actorId);

		return result;
	}

	protected Box findSpamBoxFromActor(final int actorId) {
		Box result;

		result = this.boxRepository.findSpamBoxFromActor(actorId);

		return result;
	}

	protected Box findTrashBoxFromActor(final int actorId) {
		Box result;

		result = this.boxRepository.findTrashBoxFromActor(actorId);

		return result;
	}

	protected Box findNotificationBoxFromActor(final int actorId) {
		Box result;

		result = this.boxRepository.findNotificationBoxFromActor(actorId);

		return result;
	}

	protected void addMessage(final Box box, final Message message) {
		box.getMessages().add(message);
	}

	protected void removeMessage(final Box box, final Message message) {
		box.getMessages().remove(message);
	}

	protected void checkByPrincipal(final Box box) {
		Actor principal;

		principal = this.actorService.findPrincipal();

		Assert.isTrue(box.getActor().equals(principal));
	}

	// Private methods ---------------------------
	private void checkName(final Box box) {
		boolean validName;

		validName = box.getName().equals("in box") || box.getName().equals("out box") || box.getName().equals("notification box") || box.getName().equals("trash box") || box.getName().equals("spam box");

		Assert.isTrue(!validName);
	}

}
