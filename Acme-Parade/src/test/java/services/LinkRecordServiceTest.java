
package services;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Brotherhood;
import domain.LinkRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class LinkRecordServiceTest extends AbstractTest {

	// Services under test --------------------------

	@Autowired
	private LinkRecordService	linkRecordService;

	// Other supporting services --------------------

	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Suite test -----------------------------------

	@Test
	public void driverCreate() {
		final Object testingData[][] = {

			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede crear linkRecord
			 * C: 80%(16/20) Recorre 16 lineas de código de las 20 posibles
			 * D:16.6%, (1/6) hay 3 atributos pueden tomar valadores en blanco o no
			 */
			{
				"brotherhood1", " title linkRecord", "text linkRecord", "brotherhood2", null
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede crear linkRecord
			 * B: Linkearse a él mismo
			 * C: 35%(7/20) Recorre 7 lineas de código de las 20 posibles
			 * D:16.6%, (1/6) hay 3 atributos pueden tomar valadores en blanco o no
			 */
			{
				"brotherhood1", "title linkRecord testd", "text linkRecord test", "brotherhood1", IllegalArgumentException.class
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede crear linkRecord
			 * B: Crear un linkRecord sin título
			 * C: 80%(16/20) Recorre 16 lineas de código de las 20 posibles
			 * D:16.6%, (1/6) hay 3 atributos pueden tomar valadores en blanco o no
			 */
			{
				"brotherhood1", "", "text linkRecord test", "brotherhood2", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede crear linkRecord
			 * B: Crear un texto sin título
			 * C: 80%(16/20) Recorre 16 lineas de código de las 20 posibles
			 * D:16.6%, (1/6) hay 3 atributos pueden tomar valadores en blanco o no
			 */
			{
				"brotherhood1", "title linkRecord test", "", "brotherhood2", ConstraintViolationException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], super.getEntityId((String) testingData[i][3]), (Class<?>) testingData[i][4]);

	}

	protected void templateCreate(final String username, final String titleLink, final String textLink, final Integer brotherhoodId, final Class<?> expected) {
		Class<?> caught;
		LinkRecord linkRecord;
		LinkRecord linkRecordSaved;
		final Brotherhood brotherhood;

		this.startTransaction();

		caught = null;
		try {
			super.authenticate(username);

			linkRecord = this.linkRecordService.create();

			linkRecord.setText(textLink);
			linkRecord.setTitle(titleLink);
			brotherhood = this.brotherhoodService.findOne(brotherhoodId);
			linkRecord.setBrotherhood(brotherhood);

			linkRecordSaved = this.linkRecordService.save(linkRecord);

			this.linkRecordService.flush();

			Assert.notNull(linkRecordSaved);

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	@Test
	public void driverEdit() {
		final Object testingData[][] = {
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede editar sus linkRecords
			 * C: 85%(17/20) Recorre 17 lineas de código de las 20 posibles
			 * D:16.6%, (1/6) hay 3 atributos pueden tomar valadores en blanco o no
			 */
			{
				"brotherhood1", "linkRecord2", "linkRecord test", "linkRecord test", "brotherhood2", null
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede editar sus linkRecords
			 * B: Linkearse a él mismo
			 * C: 35%(7/20) Recorre 7 lineas de código de las 20 posibles
			 * D:16.6%, (1/6) hay 3 atributos pueden tomar valadores en blanco o no
			 */
			{
				"brotherhood1", "linkRecord2", "linkRecord test", "linkRecord test", "brotherhood1", IllegalArgumentException.class
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede editar sus linkRecords
			 * B: Editar un linkRecord sin título
			 * C: 85%(17/20) Recorre 17 lineas de código de las 20 posibles
			 * D:16.6%, (1/6) hay 3 atributos pueden tomar valadores en blanco o no
			 */
			{
				"brotherhood1", "linkRecord2", "", "linkRecord test", "brotherhood2", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede editar sus linkRecords
			 * B: Editar un linkRecord sin texto
			 * C: 85%(17/20) Recorre 17 lineas de código de las 20 posibles
			 * D:16.6%, (1/6) hay 3 atributos pueden tomar valadores en blanco o no
			 */
			{
				"brotherhood1", "linkRecord2", "inceptionRecordTest1", "", "brotherhood2", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede editar sus linkRecords
			 * B: Editar un linkRecord de otro usuario
			 * C: 80%(16/20) Recorre 16 lineas de código de las 20 posibles
			 * D:16.6%, (1/6) hay 3 atributos pueden tomar valadores en blanco o no
			 */
			{
				"brotherhood1", "linkRecord3", "inceptionRecordTest1", "textInceptionRecordTest1", "brotherhood3", IllegalArgumentException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreateEdit((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (String) testingData[i][3], super.getEntityId((String) testingData[i][4]), (Class<?>) testingData[i][5]);

	}
	protected void templateCreateEdit(final String username, final Integer linkRecordId, final String titleLink, final String textLink, final Integer brotherhoodId, final Class<?> expected) {
		Class<?> caught;
		LinkRecord linkRecord;
		LinkRecord linkRecordSaved;
		final Brotherhood brotherhood;

		this.startTransaction();

		caught = null;
		try {
			super.authenticate(username);

			linkRecord = this.linkRecordService.findOneEdit(linkRecordId);

			linkRecord.setText(textLink);
			linkRecord.setTitle(titleLink);
			brotherhood = this.brotherhoodService.findOne(brotherhoodId);
			linkRecord.setBrotherhood(brotherhood);

			linkRecordSaved = this.linkRecordService.save(linkRecord);

			this.linkRecordService.flush();

			Assert.notNull(linkRecordSaved);

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * A: Req1, 3.1: Un actor autenticado como brotherhod puede borrar sus linkRecords
	 * C: 100%(11/11) Recorre 11 lineas de código de las 11 posibles
	 * D:Intencionadamente en blanco; no hay datos que checkear
	 */
	@Test
	public void delete_positive_test() {
		super.authenticate("brotherhood1");

		int linkRecordId;
		LinkRecord linkRecord;

		linkRecordId = super.getEntityId("linkRecord1");
		linkRecord = this.linkRecordService.findOne(linkRecordId);

		this.linkRecordService.delete(linkRecord);

		super.unauthenticate();
	}
	/*
	 * A: Req1, 3.1: Un actor autenticado como brotherhod puede borrar sus linkRecords
	 * B: Un usuario borrar el linkRecord de otro usuario
	 * C: 54.5%(6/11) Recorre 6 lineas de código de las 11 posibles
	 * D:Intencionadamente en blanco; no hay datos que checkear
	 */
	@Test(expected = IllegalArgumentException.class)
	public void delete_negative_test() {
		super.authenticate("brotherhood1");

		int linkRecordId;
		LinkRecord linkRecord;

		linkRecordId = super.getEntityId("linkRecord3");
		linkRecord = this.linkRecordService.findOne(linkRecordId);

		this.linkRecordService.delete(linkRecord);

		super.unauthenticate();
	}

	/*
	 * A: Req1, 3.1: Un actor autenticado como brotherhod puede mostrar sus linkRecords
	 * C: 100%(5/5) Recorre 5 lineas de código de las 5 posibles
	 * D:Intencionadamente en blanco; no hay datos que checkear
	 */
	@Test
	public void displayAuthenticated_positive_test() {
		super.authenticate("member2");

		final int linkRecordId;
		LinkRecord linkRecord;

		linkRecordId = super.getEntityId("linkRecord1");
		linkRecord = this.linkRecordService.findOne(linkRecordId);

		Assert.notNull(linkRecord);

		super.unauthenticate();
	}

	/*
	 * A: Req2.1: Un actor no autenticado puede mostrar linkRecords de las historias de los brotherhood
	 * C: 100%(5/5) Recorre 5 lineas de código de las 5 posibles
	 * D: Intencionadamente en blanco; no hay datos que checkear
	 */
	@Test
	public void displayNotAuthenticated_positive_test() {

		final int linkRecordId;
		LinkRecord linkRecord;

		linkRecordId = super.getEntityId("linkRecord1");
		linkRecord = this.linkRecordService.findOne(linkRecordId);

		Assert.notNull(linkRecord);

	}

}
