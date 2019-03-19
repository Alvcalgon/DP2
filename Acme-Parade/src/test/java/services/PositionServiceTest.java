
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
import domain.Position;
import domain.TranslationPosition;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class PositionServiceTest extends AbstractTest {

	// Services under test --------------------------
	@Autowired
	private PositionService				positionService;

	// Other supporting services --------------------
	@Autowired
	private TranslationPositionService	translationPositionService;


	// Suite test -----------------------------------
	@Test
	public void createTest() {
		super.authenticate("admin1");

		Position position;

		position = this.positionService.create();

		Assert.notNull(position);
		Assert.notNull(position.getTranslationPositions());

		super.unauthenticate();
	}

	/*
	 * Test negativo: position es nulo.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negative_saveTest_uno() {
		super.authenticate("admin1");

		final Position position = null;
		Position saved;

		saved = this.positionService.save(position);

		Assert.isNull(saved);

		super.unauthenticate();
	}

	/*
	 * Test negativo: Position::translationPositions posee un valor
	 * incorrecto, los 2 translationPositions tiene definido el mismo
	 * idioma.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negative_saveTest_dos() {
		super.authenticate("admin1");

		Position position, saved;
		Collection<Position> all;

		position = this.positionService.create();
		position.setTranslationPositions(this.find_invalid_TP());

		saved = this.positionService.save(position);

		all = this.positionService.findAll();

		Assert.isNull(saved);
		Assert.isTrue(!all.contains(saved));

		super.unauthenticate();
	}

	/*
	 * Test positivo: se inserta correctamente una position
	 * en la BD.
	 */
	@Test
	public void positive_saveTest_uno() {
		super.authenticate("admin1");

		Position position, saved;
		Collection<Position> all;

		position = this.positionService.create();
		position.setTranslationPositions(this.find_valid_TP());

		saved = this.positionService.save(position);

		all = this.positionService.findAll();

		Assert.notNull(saved);
		Assert.isTrue(saved.getId() != 0);
		Assert.isTrue(all.contains(saved));

		super.unauthenticate();
	}

	/*
	 * Test negativo: position es nulo.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negative_deleteTest_uno() {
		super.authenticate("admin1");

		final Position position = null;
		Collection<Position> all;

		this.positionService.delete(position);

		all = this.positionService.findAll();

		Assert.isTrue(!all.contains(position));

		super.unauthenticate();
	}

	/*
	 * Test objeto: el objeto no esta persistido en la BD,
	 * por lo tanto, no se puede eliminar.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negative_deleteTest_dos() {
		super.authenticate("admin1");

		final Position position;
		Collection<Position> all;

		position = this.positionService.create();
		position.setTranslationPositions(this.find_valid_TP());

		this.positionService.delete(position);

		all = this.positionService.findAll();

		Assert.isTrue(!all.contains(position));

		super.unauthenticate();
	}

	/*
	 * Test negativo: no se puede borrar una position si esta asociada
	 * al menos a un objeto enrolment.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negative_deleteTest_tres() {
		super.authenticate("admin1");

		final Position position;
		final int positionId = super.getEntityId("position1");
		Collection<Position> all;

		position = this.positionService.findOne(positionId);

		this.positionService.delete(position);

		all = this.positionService.findAll();

		Assert.isTrue(all.contains(position));

		super.unauthenticate();
	}

	//TODO: Borrar del BD un objeto correctamente: tal y como está
	// hecho el populateDatabase.xml, no se puede borrar ningun objeto
	// de la entidad Position porque todas estan asociadas a algun
	// enrolment.

	private Collection<TranslationPosition> find_invalid_TP() {
		List<TranslationPosition> results;

		TranslationPosition tp1, tp2, saved_uno, saved_dos;

		tp1 = this.translationPositionService.create();
		tp1.setName("Costalero");
		tp1.setLanguage("en");

		tp2 = this.translationPositionService.create();
		tp2.setName("Costainer");
		tp2.setLanguage("en");

		saved_uno = this.translationPositionService.save(tp1);
		saved_dos = this.translationPositionService.save(tp2);

		results = new ArrayList<>();
		results.add(saved_uno);
		results.add(saved_dos);

		return results;
	}

	private Collection<TranslationPosition> find_valid_TP() {
		List<TranslationPosition> results;

		TranslationPosition tp1, tp2, saved_uno, saved_dos;

		tp1 = this.translationPositionService.create();
		tp1.setName("Costalero");
		tp1.setLanguage("es");

		tp2 = this.translationPositionService.create();
		tp2.setName("Costainer");
		tp2.setLanguage("en");

		saved_uno = this.translationPositionService.save(tp1);
		saved_dos = this.translationPositionService.save(tp2);

		results = new ArrayList<>();
		results.add(saved_uno);
		results.add(saved_dos);

		return results;
	}

}
