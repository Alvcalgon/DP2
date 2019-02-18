
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

		// ¿Que ocurre si borro una carpeta que contiene mensajes?

		// ¿Que ocurre si borro una carpeta que tiene carpetas dentro?

	}

	public void move(final Box target, final Box origin, final Box destination) {

	}

	// Other business methods ---------------------
	public Collection<Box> findBoxesByActor(final int actorId) {
		Collection<Box> results;

		results = this.boxRepository.findBoxesByActor(actorId);

		return results;
	}

	public Collection<Box> findChildBoxesByBox(final int boxId) {
		Collection<Box> results;

		results = this.boxRepository.findChildBoxesByBox(boxId);

		return results;
	}

	// Protected methods --------------------------
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

	// Private methods ---------------------------
	private void checkName(final Box box) {
		boolean validName;

		validName = box.getName().equals("in box") || box.getName().equals("out box") || box.getName().equals("notification box") || box.getName().equals("trash box") || box.getName().equals("spam box");

		Assert.isTrue(!validName);
	}

	private void checkByPrincipal(final Box box) {
		Actor principal;

		principal = this.actorService.findPrincipal();

		Assert.isTrue(box.getActor().equals(principal));
	}

}
