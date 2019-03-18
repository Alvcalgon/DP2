
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
			 * A: Req1, 3.1
			 * B: Crea adecuadamente un legalRecord
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "title legalRecord test", "text legalRecord test", "name legalRecord test", 21, "law test", null
			},
			/*
			 * A: Req1, 3.1
			 * B: Crear adecuadamente un legalRecord con vatNumber==0
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "title legalRecord test", "text legalRecord test", "name legalRecord test", 100, "law test", null
			},
			/*
			 * A: Req1, 3.1
			 * B: Crear adecuadamente un legalRecord con vatNumber == 100
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "title legalRecord test", "text legalRecord test", "name legalRecord test", 0, "law test", null
			},
			/*
			 * A: Req1, 3.1
			 * B: Crear un legalRecord con title en blanco
			 * C:
			 * D:
			 */
			{
				"brotherhood1", " ", "text legalRecord test", "name legalRecord test", 21, "law test", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1
			 * B: Crear un legalRecord con con text en blanco
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "title legalRecord test", "", "name legalRecord test", 21, "law test", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1
			 * B: Crear un legalRecord con name en blanco
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "title legalRecord test", "text legalRecord test", "", 21, "law test", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1
			 * B: Crear un legalRecord vatNumber negativo
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "title legalRecord test", "text legalRecord test", "", -3, "law test", ConstraintViolationException.class
			},

			/*
			 * A: Req1, 3.1
			 * B: Crear un legalRecord con laws en blanco
			 * C:
			 * D:
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
			 * A: Req1, 3.1
			 * B: Editar adecuadamente un legalRecord
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "legalRecord1", "title legalRecord test", "text legalRecord test", "name legalRecord test", 21, "law test", null
			},
			/*
			 * A: Req1, 3.1
			 * B: Editar adecuadamente un legalRecord con vatNumber==0
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "legalRecord1", "title legalRecord test", "text legalRecord test", "name legalRecord test", 100, "law test", null
			},
			/*
			 * A: Req1, 3.1
			 * B: Editar adecuadamente un legalRecord con vatNumber == 100
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "legalRecord1", "title legalRecord test", "text legalRecord test", "name legalRecord test", 0, "law test", null
			},
			/*
			 * A: Req1, 3.1
			 * B: Editar un legalRecord con title en blanco
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "legalRecord1", " ", "text legalRecord test", "name legalRecord test", 21, "law test", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1
			 * B: Editar un legalRecord con con text en blanco
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "legalRecord1", "title legalRecord test", "", "name legalRecord test", 21, "law test", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1
			 * B: Editar un legalRecord con name en blanco
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "legalRecord1", "title legalRecord test", "text legalRecord test", "", 21, "law test", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1
			 * B: Editar un legalRecord vatNumber negativo
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "legalRecord1", "title legalRecord test", "text legalRecord test", "name legalRecord test", -3, "law test", ConstraintViolationException.class
			},

			/*
			 * A: Req1, 3.1
			 * B: Editar un legalRecord con laws en blanco
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "legalRecord1", "title legalRecord test", "text legalRecord test", "name legalRecord test", 21, "", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1
			 * B: Editar un legalRecord que no es del principal
			 * C:
			 * D:
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
	 * A: Req1, 3.1
	 * B:Borrar
	 * C:
	 * D:
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
	 * A: Req1, 3.1
	 * B:Borrar uno que no es suyo
	 * C:
	 * D:
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

}
