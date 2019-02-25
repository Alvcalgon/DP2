
package services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Box;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class BoxServiceTest extends AbstractTest {

	// Service under testing -----------------
	@Autowired
	private BoxService	boxService;


	// Other services ------------------------

	// Suite test ----------------------------
	@Test
	public void testDescendantBoxes() {
		super.authenticate("member1");

		List<Box> descendant;
		final int boxId = super.getEntityId("box010");
		final Box box = this.boxService.findOne(boxId);

		descendant = new ArrayList<Box>();
		descendant.addAll(this.boxService.descendantBoxes(box));

		for (final Box b : descendant)
			System.out.println(b.getName());

		Assert.isTrue(descendant.size() > 0);

		super.unauthenticate();
	}

	@Test
	public void testCreate() {
		super.authenticate("member2");

		Box box;

		box = this.boxService.create();

		Assert.notNull(box);
		Assert.notNull(box.getActor());
		Assert.notNull(box.getMessages());
		Assert.isNull(box.getName());
		Assert.isNull(box.getParent());

		super.unauthenticate();
	}

	/*
	 * Test negativo: se intenta editar una systemBox
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negative_saveTest_uno() {
		super.authenticate("member2");

		final int memberId = super.getEntityId("member2");
		Box saved, inBox;

		inBox = this.boxService.findInBoxFromActor(memberId);
		inBox.setName("in box Hacked");

		saved = this.boxService.save(inBox);

		Assert.isNull(saved);

		super.unauthenticate();
	}

	/*
	 * Test negativo: se intenta editar una systemBox
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negative_saveTest_dos() {
		super.authenticate("member2");

		Box box, saved;

		box = this.boxService.create();
		box.setName("in box");

		saved = this.boxService.save(box);

		Assert.isNull(saved);

		super.unauthenticate();
	}

	/*
	 * Test negativo: se intenta editar una systemBox
	 */
	@Test
	public void positive_saveTest_uno() {
		super.authenticate("member2");

		Box box, saved;

		box = this.boxService.create();
		box.setName("Curso 18/19");

		saved = this.boxService.save(box);

		Assert.notNull(saved);
		Assert.isTrue(saved.getId() != 0);

		super.unauthenticate();
	}

	/*
	 * Test negativo: se trata de eliminar una carpeta
	 * predefinida.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negative_deleteTest_uno() {
		super.authenticate("member2");

		final int memberId = super.getEntityId("member2");
		Box inBox, found;

		inBox = this.boxService.findInBoxFromActor(memberId);
		this.boxService.delete(inBox);

		found = this.boxService.findOne(inBox.getId());
		Assert.notNull(found);

		super.unauthenticate();
	}

	/*
	 * Test negativo: member2 trata de eliminar una carpeta
	 * cuyo propietario es brotherhood1
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negative_deleteTest_dos() {
		super.authenticate("member2");

		final int boxId = super.getEntityId("box01");
		Box box, found;

		box = this.boxService.findOne(boxId);
		this.boxService.delete(box);

		found = this.boxService.findOne(box.getId());
		Assert.notNull(found);

		super.unauthenticate();
	}

	/*
	 * Test positivo: brotherhood borra con exito
	 * una de sus carpetas personalizables.
	 */
	@Test
	public void negative_deleteTest_tres() {
		super.authenticate("brotherhood1");

		final int boxId = super.getEntityId("box01");
		Box box, found;

		box = this.boxService.findOne(boxId);
		this.boxService.delete(box);

		found = this.boxService.findOne(box.getId());
		Assert.isNull(found);

		super.unauthenticate();
	}

}
