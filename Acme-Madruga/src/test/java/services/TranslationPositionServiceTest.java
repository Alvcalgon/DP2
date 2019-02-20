
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.TranslationPosition;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class TranslationPositionServiceTest extends AbstractTest {

	// Services under test -------------------------
	@Autowired
	private TranslationPositionService	translationPositionService;


	// Other supporting test -----------------------

	// Suite tests ---------------------------------
	@Test
	public void testCreate() {
		super.authenticate("admin1");

		TranslationPosition translationPosition;

		translationPosition = this.translationPositionService.create();

		Assert.notNull(translationPosition);
		Assert.isNull(translationPosition.getName());
		Assert.isNull(translationPosition.getLanguage());

		super.unauthenticate();
	}

	/*
	 * Test negativo: translationPosition es nulo.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negative_saveTest_uno() {
		super.authenticate("admin1");

		TranslationPosition translationPosition, saved;

		translationPosition = null;

		saved = this.translationPositionService.save(translationPosition);

		Assert.isNull(saved);

		super.unauthenticate();
	}

	/*
	 * Test negativo: el idioma del objeto no es soportado el sistema
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negative_saveTest_dos() {
		super.authenticate("admin1");

		TranslationPosition translationPosition, saved;

		translationPosition = this.translationPositionService.create();
		translationPosition.setName("Costalero");
		translationPosition.setLanguage("fr");

		saved = this.translationPositionService.save(translationPosition);

		Assert.isNull(saved);

		super.unauthenticate();
	}

	// Test negativo: ya existe en la BD otro objeto con dicho nombre
	// e idioma.
	@Test(expected = IllegalArgumentException.class)
	public void negative_saveTest_tres() {
		super.authenticate("admin1");

		TranslationPosition translationPosition, saved;

		translationPosition = this.translationPositionService.create();
		translationPosition.setName("President");
		translationPosition.setLanguage("en");

		saved = this.translationPositionService.save(translationPosition);

		Assert.isNull(saved);

		super.unauthenticate();
	}

	/*
	 * Se inserta correctamente una nueva translationPosition.
	 */
	@Test
	public void positive_saveTest_uno() {
		super.authenticate("admin1");

		TranslationPosition translationPosition, saved, found;

		translationPosition = this.translationPositionService.create();
		translationPosition.setName("Costalero");
		translationPosition.setLanguage("es");

		saved = this.translationPositionService.save(translationPosition);
		found = this.translationPositionService.findOne(saved.getId());

		Assert.notNull(saved);
		Assert.notNull(found);

		super.unauthenticate();
	}

	/*
	 * Test negativo: translationPosition es nulo.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negative_deleteTest_uno() {
		super.authenticate("admin1");

		final TranslationPosition translationPosition = null;
		Collection<TranslationPosition> all;

		this.translationPositionService.delete(translationPosition);

		all = this.translationPositionService.findAll();

		Assert.isTrue(!all.contains(translationPosition));

		super.unauthenticate();
	}

	/*
	 * Test negativo: se trata de borrar de la BD un objeto que
	 * ni siquiera existe en la BD.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negative_deleteTest_dos() {
		super.authenticate("admin1");

		TranslationPosition translationPosition;
		Collection<TranslationPosition> all;

		translationPosition = this.translationPositionService.create();
		translationPosition.setLanguage("es");
		translationPosition.setName("Costalero");

		this.translationPositionService.delete(translationPosition);

		all = this.translationPositionService.findAll();

		Assert.isTrue(!all.contains(translationPosition));

		super.unauthenticate();
	}

	/*
	 * Test positivo: Se borra existosamente un objeto de la BD.
	 */
	@Test
	public void positive_deleteTest_uno() {
		super.authenticate("admin1");

		int translationPositionId;
		TranslationPosition translationPosition, deleted;
		;

		translationPositionId = super.getEntityId("translationPosition1");
		translationPosition = this.translationPositionService.findOne(translationPositionId);

		this.translationPositionService.delete(translationPosition);

		deleted = this.translationPositionService.findOne(translationPositionId);

		Assert.isNull(deleted);

		super.unauthenticate();
	}

}
