
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
import domain.Box;
import domain.Message;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class MessageServiceTest extends AbstractTest {

	// Service under testing -----------------------------------
	@Autowired
	private MessageService	messageService;

	// Other services ------------------------------------------
	@Autowired
	private ActorService	actorService;

	@Autowired
	private BoxService		boxService;


	// Suite test ---------------------------------------------

	@Test
	public void createTest() {
		super.authenticate("member1");

		Message message;

		message = this.messageService.create();

		Assert.notNull(message);
		Assert.notNull(message.getSender());
		Assert.notNull(message.getRecipients());
		Assert.notNull(message.getIsSpam());
		Assert.notNull(message.getSentMoment());
		Assert.isNull(message.getBody());
		Assert.isNull(message.getPriority());
		Assert.isNull(message.getSubject());
		Assert.isNull(message.getTags());

		super.unauthenticate();
	}

	/*
	 * Test negativo: message is null.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negative_sendTest_uno() {
		super.authenticate("member1");

		final Message message = null;
		Message sent;

		sent = this.messageService.send(message);

		Assert.isNull(sent);

		super.unauthenticate();
	}

	/*
	 * Test negativo: se trata de editar un mensaje
	 * de la BD
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negative_sendTest_dos() {
		super.authenticate("member1");

		final int messageId = super.getEntityId("message1");
		Message message = null, sent;

		message = this.messageService.findOne(messageId);
		message.setBody("Body edited");
		message.setSubject("Subject edited");

		sent = this.messageService.send(message);

		Assert.isNull(sent);

		super.unauthenticate();
	}

	/*
	 * Test negativo: el sender no coincide con el principal
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negative_sendTest_tres() {
		super.authenticate("member1");

		final List<Actor> recipients = new ArrayList<Actor>();

		final int recipientId = super.getEntityId("administrator1");
		final int actorId = super.getEntityId("member2");
		final Actor sender = this.actorService.findOne(actorId);
		final Actor recipient = this.actorService.findOne(recipientId);

		recipients.add(recipient);

		Message message, sent;

		message = this.messageService.create();
		message.setSender(sender);
		message.setRecipients(recipients);
		message.setSubject("Subject Test");
		message.setBody("Body Test");
		message.setPriority("NEUTRAL");

		sent = this.messageService.send(message);

		Assert.isNull(sent);

		super.unauthenticate();
	}

	/*
	 * Test negativo: prioridad no valida.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negative_sendTest_cuatro() {
		super.authenticate("member1");

		final List<Actor> recipients = new ArrayList<Actor>();

		final int recipientId = super.getEntityId("administrator1");
		final Actor recipient = this.actorService.findOne(recipientId);
		recipients.add(recipient);

		Message message, sent;

		message = this.messageService.create();
		message.setRecipients(recipients);
		message.setSubject("Subject Test");
		message.setBody("Body Test");
		message.setPriority("TEST");

		sent = this.messageService.send(message);

		Assert.isNull(sent);

		super.unauthenticate();
	}

	/*
	 * Test positivo: Se envia correctamente un mensaje. Dicho mensaje
	 * no contiene palabra spam, por tanto, se almacenara en la
	 * bandeja de entrada del receptor.
	 */
	@Test
	public void positive_sendTest_uno() {
		super.authenticate("member1");

		Box outBoxSender, inBoxRecipient;
		final List<Actor> recipients = new ArrayList<Actor>();
		final int recipientId = super.getEntityId("administrator1");
		final Actor recipient = this.actorService.findOne(recipientId);
		recipients.add(recipient);

		Message message, sent, found;

		message = this.messageService.create();
		message.setRecipients(recipients);
		message.setSubject("Subject Test");
		message.setBody("Body Test");
		message.setPriority("LOW");

		sent = this.messageService.send(message);
		found = this.messageService.findOne(sent.getId());

		Assert.notNull(sent);
		Assert.notNull(found);

		outBoxSender = this.boxService.findOutBoxFromActor(sent.getSender().getId());
		inBoxRecipient = this.boxService.findInBoxFromActor(recipientId);

		Assert.isTrue(outBoxSender.getMessages().contains(sent) && inBoxRecipient.getMessages().contains(sent));

		super.unauthenticate();
	}

	/*
	 * Test positivo: Se envia correctamente un mensaje. Dicho mensaje
	 * contiene palabra spam, por tanto, se almacenara en la
	 * bandeja de spam de los receptores.
	 */
	@Test
	public void positive_sendTest_dos() {
		super.authenticate("member1");

		Box outBoxSender, spamBoxRecipientUno, spamBoxRecipientDos;
		final List<Actor> recipients = new ArrayList<Actor>();

		int recipientId = super.getEntityId("administrator1");
		final Actor recipientUno = this.actorService.findOne(recipientId);

		recipientId = super.getEntityId("member2");
		final Actor recipientDos = this.actorService.findOne(recipientId);

		recipients.add(recipientUno);
		recipients.add(recipientDos);

		Message message, sent, found;

		message = this.messageService.create();
		message.setRecipients(recipients);
		message.setSubject("Subject Test-sex");
		message.setBody("Body Test. ViaGra");
		message.setPriority("LOW");

		sent = this.messageService.send(message);
		found = this.messageService.findOne(sent.getId());

		Assert.notNull(sent);
		Assert.notNull(found);

		outBoxSender = this.boxService.findOutBoxFromActor(sent.getSender().getId());
		spamBoxRecipientUno = this.boxService.findSpamBoxFromActor(recipientUno.getId());
		spamBoxRecipientDos = this.boxService.findSpamBoxFromActor(recipientDos.getId());

		Assert.isTrue(outBoxSender.getMessages().contains(sent) && spamBoxRecipientUno.getMessages().contains(sent) && spamBoxRecipientDos.getMessages().contains(sent));

		super.unauthenticate();
	}

	/*
	 * Test negativo: el mensaje no se encuentra en la BD.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negative_deleteTest_uno() {
		super.authenticate("member1");

		final List<Actor> recipients = new ArrayList<Actor>();
		final int recipientId = super.getEntityId("administrator1");
		final Actor recipient = this.actorService.findOne(recipientId);
		recipients.add(recipient);

		Box box;
		Message message;

		message = this.messageService.create();
		message.setRecipients(recipients);
		message.setSubject("Subject Test");
		message.setBody("Body Test");
		message.setPriority("LOW");

		box = this.boxService.findInBoxFromActor(message.getSender().getId());

		this.messageService.delete(message, box);

		super.unauthenticate();
	}

	/*
	 * Test negativo: la bandeja no existe en la BD.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negative_deleteTest_dos() {
		super.authenticate("member1");

		Box box;
		final int messageId = super.getEntityId("message8");
		final Message message = this.messageService.findOne(messageId);

		box = this.boxService.create();
		box.setName("name TEST");

		this.messageService.delete(message, box);

		super.unauthenticate();
	}

	/*
	 * Test negativo: el mensaje que se quiere borrar no
	 * se encuentra en la bandeja especificada.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negative_deleteTest_tres() {
		super.authenticate("member1");

		final int boxId = super.getEntityId("box7");
		final Box box = this.boxService.findOne(boxId);

		final int messageId = super.getEntityId("message8");
		final Message message = this.messageService.findOne(messageId);

		this.messageService.delete(message, box);

		super.unauthenticate();
	}

	/*
	 * Test negativo: el actor que quiere borrar el mensaje
	 * no tiene derecho a hacerlo puesto que no es ni el emisor
	 * ni ninguno de los receptores. Sender: member5 y
	 * Recipients = {member1}
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negative_deleteTest_cuatro() {
		super.authenticate("member2");

		final int boxId = super.getEntityId("box6");
		final Box box = this.boxService.findOne(boxId);

		final int messageId = super.getEntityId("message8");
		final Message message = this.messageService.findOne(messageId);

		this.messageService.delete(message, box);

		super.unauthenticate();
	}

	/*
	 * Test negativo: member1 quiere borrar el mensaje
	 * de la carpeta box16, el problema es que esa carpeta
	 * pertenece al emisor del mensaje: brotherhood1
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negative_deleteTest_cinco() {
		super.authenticate("member1");

		final int boxId = super.getEntityId("box16");
		final Box box = this.boxService.findOne(boxId);

		final int messageId = super.getEntityId("message6");
		final Message message = this.messageService.findOne(messageId);

		this.messageService.delete(message, box);

		super.unauthenticate();
	}

	/*
	 * Test positivo: el menesaje se borra
	 * de la carpeta.
	 */
	@Test
	public void positive_deleteTest_uno() {
		super.authenticate("member1");

		final int memberId = super.getEntityId("member1");
		final int boxId = super.getEntityId("box7");
		final Box box = this.boxService.findOne(boxId);
		final Box trashBox = this.boxService.findTrashBoxFromActor(memberId);

		final int messageId = super.getEntityId("message6");
		final Message message = this.messageService.findOne(messageId);

		this.messageService.delete(message, box);

		Assert.isTrue(!box.getMessages().contains(message));
		Assert.isTrue(trashBox.getMessages().contains(message));

		super.unauthenticate();
	}

	/*
	 * Test positivo: el menesaje se borra
	 * de la carpeta trashbox de brotherhood2.
	 */
	@Test
	public void positive_deleteTest_dos() {
		super.authenticate("brotherhood2");

		final int brotherhoodId = super.getEntityId("brotherhood2");
		final int boxId = super.getEntityId("box14");
		final Box box = this.boxService.findOne(boxId);
		final Box trashBox = this.boxService.findTrashBoxFromActor(brotherhoodId);

		final int messageId = super.getEntityId("message4");
		final Message message = this.messageService.findOne(messageId);

		this.messageService.delete(message, trashBox);

		Assert.isTrue(box.getMessages().contains(message));
		Assert.isTrue(!trashBox.getMessages().contains(message));

		super.unauthenticate();
	}

	/*
	 * Test positivo: el menesaje se borra
	 * de la carpeta trashbox de brotherhood2 y de la carpeta
	 * spam box de member2.
	 */
	@Test
	public void positive_deleteTest_tres() {
		super.authenticate("brotherhood2");

		final int brotherhoodId = super.getEntityId("brotherhood2");
		final int boxId = super.getEntityId("box14");
		final Box box = this.boxService.findOne(boxId);
		Box trashBox = this.boxService.findTrashBoxFromActor(brotherhoodId);

		final int messageId = super.getEntityId("message4");
		final Message deleted, message = this.messageService.findOne(messageId);

		this.messageService.delete(message, trashBox);

		Assert.isTrue(box.getMessages().contains(message));
		Assert.isTrue(!trashBox.getMessages().contains(message));

		super.unauthenticate();

		super.authenticate("member2");

		final int memberId = super.getEntityId("member2");
		final Box spamBox = this.boxService.findSpamBoxFromActor(memberId);

		// Justo en este punto, el mensaje solo se encuentra en una unica
		// carpeta, en la spam box de member2.

		this.messageService.delete(message, spamBox);

		trashBox = this.boxService.findTrashBoxFromActor(memberId);

		this.messageService.delete(message, trashBox);

		deleted = this.messageService.findOne(messageId);

		Assert.isNull(deleted);
		Assert.isTrue(!spamBox.getMessages().contains(message));
		Assert.isTrue(!trashBox.getMessages().contains(message));

		super.unauthenticate();
	}

	@Test
	public void positive_moveTest() {
		super.authenticate("member2");

		final int messageId = super.getEntityId("message9");
		final Message message = this.messageService.findOne(messageId);

		Box inBox, trashBox;
		final int memberId = super.getEntityId("member2");

		inBox = this.boxService.findInBoxFromActor(memberId);
		trashBox = this.boxService.findTrashBoxFromActor(memberId);

		Assert.isTrue(inBox.getMessages().contains(message));
		Assert.isTrue(!trashBox.getMessages().contains(message));

		this.messageService.moveMessage(message, inBox, trashBox);

		Assert.isTrue(!inBox.getMessages().contains(message));
		Assert.isTrue(trashBox.getMessages().contains(message));

		super.unauthenticate();
	}

	@Test
	public void positive_sendBroadcastTest_uno() {
		super.authenticate("admin1");

		Box notificationBox, outBox;
		Message message, broadcast;
		Collection<Actor> all;
		final int adminId = this.getEntityId("administrator1");

		message = this.messageService.create();
		message.setSubject("Subject TEST");
		message.setBody("Body TEST");
		message.setPriority("NEUTRAL");
		message.setTags("NUEVO ADMINISTRATOR");

		broadcast = this.messageService.sendBroadcast(message);

		outBox = this.boxService.findOutBoxFromActor(adminId);
		Assert.isTrue(outBox.getMessages().contains(broadcast));

		all = this.actorService.findAll();
		for (final Actor a : all) {
			notificationBox = this.boxService.findNotificationBoxFromActor(a.getId());

			Assert.isTrue(notificationBox.getMessages().contains(broadcast));
		}

		super.unauthenticate();
	}

	/*
	 * Test positivo: el mensaje contiene spam. Por tanto, se almacenara
	 * en la bandeja de spam de los receptores.
	 */
	@Test
	public void positive_sendBroadcastTest_dos() {
		super.authenticate("admin1");

		Box spamBox, outBox;
		Message message, broadcast;
		Collection<Actor> all;
		final int adminId = this.getEntityId("administrator1");

		message = this.messageService.create();
		message.setSubject("Subject TEST - Viagra");
		message.setBody("Body TEST - sex");
		message.setPriority("NEUTRAL");
		message.setTags("NUEVO ADMINISTRATOR");

		broadcast = this.messageService.sendBroadcast(message);

		outBox = this.boxService.findOutBoxFromActor(adminId);
		Assert.isTrue(outBox.getMessages().contains(broadcast));

		all = this.actorService.findAll();
		for (final Actor a : all) {
			spamBox = this.boxService.findSpamBoxFromActor(a.getId());

			Assert.isTrue(spamBox.getMessages().contains(broadcast));
		}

		super.unauthenticate();
	}

}
