
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
import domain.LegalRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class LegalRecordServiceTest extends AbstractTest {

	// Services under test --------------------------

	@Autowired
	private LegalRecordService	legalRecordService;


	// Other supporting services --------------------

	// Suite test -----------------------------------

	@Test
	public void driverCreate() {
		final Object testingData[][] = {
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede crear legalRecord
			 * C: 80%(16/20) Recorre 16 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 5 atributos, 4 pueden tomar valadores en blanco y otro puede tomar numeros entre 0-100, por encima y por debajo de 100 y 0 respectivamente
			 */
			{
				"brotherhood1", "title legalRecord test", "text legalRecord test", "name legalRecord test", 21, "law test", null
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede crear legalRecord
			 * C: 80%(16/20) Recorre 16 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 5 atributos, 4 pueden tomar valadores en blanco y otro puede tomar numeros entre 0-100, por encima y por debajo de 100 y 0 respectivamente
			 */
			{
				"brotherhood1", "title legalRecord test", "text legalRecord test", "name legalRecord test", 100, "law test", null
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede crear legalRecord
			 * C: 80%(16/20) Recorre 16 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 5 atributos, 4 pueden tomar valadores en blanco y otro puede tomar numeros entre 0-100, por encima y por debajo de 100 y 0 respectivamente
			 */
			{
				"brotherhood1", "title legalRecord test", "text legalRecord test", "name legalRecord test", 0, "law test", null
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede crear legalRecord
			 * B: Crear un legalRecord sin título
			 * C: 80%(16/20) Recorre 16 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 5 atributos, 4 pueden tomar valadores en blanco y otro puede tomar numeros entre 0-100, por encima y por debajo de 100 y 0 respectivamente
			 */
			{
				"brotherhood1", " ", "text legalRecord test", "name legalRecord test", 21, "law test", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede crear legalRecord
			 * B: Crear un legalRecord sin texto
			 * C: 80%(16/20) Recorre 16 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 5 atributos, 4 pueden tomar valadores en blanco y otro puede tomar numeros entre 0-100, por encima y por debajo de 100 y 0 respectivamente
			 */
			{
				"brotherhood1", "title legalRecord test", "", "name legalRecord test", 21, "law test", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede crear legalRecord
			 * B: Crear un legalRecord sin name
			 * C: 80%(16/20) Recorre 16 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 5 atributos, 4 pueden tomar valadores en blanco y otro puede tomar numeros entre 0-100, por encima y por debajo de 100 y 0 respectivamente
			 */
			{
				"brotherhood1", "title legalRecord test", "text legalRecord test", "", 21, "law test", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede crear legalRecord
			 * B: Crear un legalRecord con vatNumber negativo
			 * C: 80%(16/20) Recorre 16 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 5 atributos, 4 pueden tomar valadores en blanco y otro puede tomar numeros entre 0-100, por encima y por debajo de 100 y 0 respectivamente
			 */
			{
				"brotherhood1", "title legalRecord test", "text legalRecord test", "", -3, "law test", ConstraintViolationException.class
			},

			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede crear legalRecord
			 * B: Crear un legalRecord sin laws
			 * C: 80%(16/20) Recorre 16 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 5 atributos, 4 pueden tomar valadores en blanco y otro puede tomar numeros entre 0-100, por encima y por debajo de 100 y 0 respectivamente
			 */
			{
				"brotherhood1", "title legalRecord test", "text legalRecord test", "", 21, "", ConstraintViolationException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Integer) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);

	}

	protected void templateCreate(final String username, final String title, final String text, final String name, final Integer vatNumber, final String law, final Class<?> expected) {
		Class<?> caught;
		LegalRecord legalRecord;
		LegalRecord legalRecordSaved;

		this.startTransaction();

		caught = null;
		try {
			super.authenticate(username);

			legalRecord = this.legalRecordService.create();

			legalRecord.setText(text);
			legalRecord.setTitle(title);
			legalRecord.setLaws(law);
			legalRecord.setName(name);
			legalRecord.setVatNumber(vatNumber);

			legalRecordSaved = this.legalRecordService.save(legalRecord);

			this.legalRecordService.flush();

			Assert.notNull(legalRecordSaved);

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
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede editar sus legalRecord
			 * C: 85%(17/20) Recorre 16 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 5 atributos, 4 pueden tomar valadores en blanco y otro puede tomar numeros entre 0-100, por encima y por debajo de 100 y 0 respectivamente
			 */
			{
				"brotherhood1", "legalRecord1", "title legalRecord test", "text legalRecord test", "name legalRecord test", 21, "law test", null
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede editar sus legalRecord
			 * C: 85%(17/20) Recorre 16 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 5 atributos, 4 pueden tomar valadores en blanco y otro puede tomar numeros entre 0-100, por encima y por debajo de 100 y 0 respectivamente
			 */
			{
				"brotherhood1", "legalRecord1", "title legalRecord test", "text legalRecord test", "name legalRecord test", 100, "law test", null
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede editar sus legalRecord
			 * C: 85%(17/20) Recorre 16 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 5 atributos, 4 pueden tomar valadores en blanco y otro puede tomar numeros entre 0-100, por encima y por debajo de 100 y 0 respectivamente
			 */
			{
				"brotherhood1", "legalRecord1", "title legalRecord test", "text legalRecord test", "name legalRecord test", 0, "law test", null
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede editar sus legalRecord
			 * B: Editar un legalRecord sin title
			 * C: 85%(17/20) Recorre 16 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 5 atributos, 4 pueden tomar valadores en blanco y otro puede tomar numeros entre 0-100, por encima y por debajo de 100 y 0 respectivamente
			 */
			{
				"brotherhood1", "legalRecord1", " ", "text legalRecord test", "name legalRecord test", 21, "law test", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede editar sus legalRecord
			 * B: Editar un legalRecord sin texto
			 * C: 85%(17/20) Recorre 16 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 5 atributos, 4 pueden tomar valadores en blanco y otro puede tomar numeros entre 0-100, por encima y por debajo de 100 y 0 respectivamente
			 */
			{
				"brotherhood1", "legalRecord1", "title legalRecord test", "", "name legalRecord test", 21, "law test", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede editar sus legalRecord
			 * B: Editar un legalRecord sin name
			 * C: 85%(17/20) Recorre 16 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 5 atributos, 4 pueden tomar valadores en blanco y otro puede tomar numeros entre 0-100, por encima y por debajo de 100 y 0 respectivamente
			 */
			{
				"brotherhood1", "legalRecord1", "title legalRecord test", "text legalRecord test", "", 21, "law test", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede editar sus legalRecord
			 * B: Editar un legalRecord con vatNUmber negativo
			 * C: 85%(17/20) Recorre 16 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 5 atributos, 4 pueden tomar valadores en blanco y otro puede tomar numeros entre 0-100, por encima y por debajo de 100 y 0 respectivamente
			 */
			{
				"brotherhood1", "legalRecord1", "title legalRecord test", "text legalRecord test", "name legalRecord test", -3, "law test", ConstraintViolationException.class
			},

			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede editar sus legalRecord
			 * B: Editar un legalRecord sin laws
			 * C: 85%(17/20) Recorre 16 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 5 atributos, 4 pueden tomar valadores en blanco y otro puede tomar numeros entre 0-100, por encima y por debajo de 100 y 0 respectivamente
			 */
			{
				"brotherhood1", "legalRecord1", "title legalRecord test", "text legalRecord test", "name legalRecord test", 21, "", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede editar sus legalRecord
			 * B: Editar un legalRecord de otro usuario
			 * C: 80%(16/20) Recorre 16 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 5 atributos, 4 pueden tomar valadores en blanco y otro puede tomar numeros entre 0-100, por encima y por debajo de 100 y 0 respectivamente
			 */
			{
				"brotherhood1", "legalRecord3", "title legalRecord test", "text legalRecord test", "name legalRecord test", 21, "law test", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5], (String) testingData[i][6],
				(Class<?>) testingData[i][7]);

	}
	protected void templateEdit(final String username, final Integer legalRecordId, final String title, final String text, final String name, final Integer vatNumber, final String law, final Class<?> expected) {
		Class<?> caught;
		LegalRecord legalRecord;
		LegalRecord legalRecordSaved;

		this.startTransaction();

		caught = null;
		try {
			super.authenticate(username);

			legalRecord = this.legalRecordService.findOneEdit(legalRecordId);

			legalRecord.setText(text);
			legalRecord.setTitle(title);
			legalRecord.setLaws(law);
			legalRecord.setName(name);
			legalRecord.setVatNumber(vatNumber);

			legalRecordSaved = this.legalRecordService.save(legalRecord);

			this.legalRecordService.flush();

			Assert.notNull(legalRecordSaved);

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * A: Req1, 3.1: Un actor autenticado como brotherhod puede borrar sus legalRecords
	 * C: 100%(11/11) Recorre 11 lineas de código de las 11 posibles
	 * D:Intencionadamente en blanco; no hay datos que checkear
	 */
	@Test
	public void delete_positive_test() {
		super.authenticate("brotherhood1");

		int legalRecordId;
		LegalRecord legalRecord;

		legalRecordId = super.getEntityId("legalRecord1");
		legalRecord = this.legalRecordService.findOne(legalRecordId);

		this.legalRecordService.delete(legalRecord);

		super.unauthenticate();
	}
	/*
	 * A: Req1, 3.1: Un actor autenticado como brotherhod puede borrar sus legalRecords
	 * B: Un usuario borrar el legalRecord de otro usuario
	 * C: 54.5%(6/11) Recorre 6 lineas de código de las 11 posibles
	 * D:Intencionadamente en blanco; no hay datos que checkear
	 */
	@Test(expected = IllegalArgumentException.class)
	public void delete_negative_test() {
		super.authenticate("brotherhood1");

		int legalRecordId;
		LegalRecord legalRecord;

		legalRecordId = super.getEntityId("legalRecord3");
		legalRecord = this.legalRecordService.findOne(legalRecordId);

		this.legalRecordService.delete(legalRecord);

		super.unauthenticate();
	}
	/*
	 * A: Req1, 3.1: Un actor autenticado como brotherhod puede mostrar sus legalRecords
	 * C: 100%(5/5) Recorre 5 lineas de código de las 5 posibles
	 * D:Intencionadamente en blanco; no hay datos que checkear
	 */
	@Test
	public void displayAuthenticated_positive_test() {
		super.authenticate("member2");

		final int legalRecordId;
		LegalRecord legalRecord;

		legalRecordId = super.getEntityId("legalRecord1");
		legalRecord = this.legalRecordService.findOne(legalRecordId);

		Assert.notNull(legalRecord);

		super.unauthenticate();
	}

	/*
	 * A: Req2.1: Un actor no autenticado puede mostrar legalRecords de las historias de los brotherhood
	 * C: 100%(5/5) Recorre 5 lineas de código de las 5 posibles
	 * D: Intencionadamente en blanco; no hay datos que checkear
	 */
	@Test
	public void displayNotAuthenticated_positive_test() {

		final int legalRecordId;
		LegalRecord legalRecord;

		legalRecordId = super.getEntityId("legalRecord1");
		legalRecord = this.legalRecordService.findOne(legalRecordId);

		Assert.notNull(legalRecord);
	}
}
