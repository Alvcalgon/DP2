
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
import domain.PeriodRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class PeriodRecordServiceTest extends AbstractTest {

	// Services under test --------------------------

	@Autowired
	private PeriodRecordService	periodRecordService;


	// Other supporting services --------------------

	// Suite test -----------------------------------

	@Test
	public void driverCreate() {
		final Object testingData[][] = {
			/*
			 * A: Req1, 3.1
			 * B: Crea adecuadamente un periodRecord
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "title periodRecord test", "text periodRecord test", 1966, 2000, "http://asdf.com", null
			},
			/*
			 * A: Req1, 3.1
			 * B: Crear adecuadamente un periodRecord con startYear==1 y endYeard==1
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "title periodRecord test", "text periodRecord test", 1966, 2000, "http://asdf.com", null
			},

			/*
			 * A: Req1, 3.1
			 * B: Crear un periodRecord con title en blanco
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "", "text periodRecord test", 1966, 2000, "http://asdf.com", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1
			 * B: Crear un periodRecord con con text en blanco
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "title periodRecord test", "", 1966, 2000, "http://asdf.com", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1
			 * B: Crear un periodRecord con photo en blanco
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "title periodRecord test", "text periodRecord test", 1966, 2000, "", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1
			 * B: Crear un periodRecord con photo invalida
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "title periodRecord test", "text periodRecord test", 1966, 2000, "esto no es foto", IllegalArgumentException.class
			},
			/*
			 * A: Req1, 3.1
			 * B: Crear un legalRecord startYear cero
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "title periodRecord test", "text periodRecord test", 0, 2000, "http://asdf.com", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1
			 * B: Crear un legalRecord endYear menor que startYear
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "title periodRecord test", "text periodRecord test", 1966, 1700, "http://asdf.com", IllegalArgumentException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);

	}
	protected void templateCreate(final String username, final String title, final String text, final Integer startYear, final Integer endYear, final String photo, final Class<?> expected) {
		Class<?> caught;
		PeriodRecord periodRecord;
		PeriodRecord periodRecordSaved;

		this.startTransaction();

		caught = null;
		try {
			super.authenticate(username);

			periodRecord = this.periodRecordService.create();

			periodRecord.setText(text);
			periodRecord.setTitle(title);
			periodRecord.setEndYear(endYear);
			periodRecord.setPhotos(photo);
			periodRecord.setStartYear(startYear);

			periodRecordSaved = this.periodRecordService.save(periodRecord);

			this.periodRecordService.flush();

			Assert.notNull(periodRecordSaved);

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
			 * B: Editar adecuadamente un periodRecord
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "periodRecord1", "title periodRecord test", "text periodRecord test", 1966, 2000, "http://asdf.com", null
			},
			/*
			 * A: Req1, 3.1
			 * B: Editar adecuadamente un periodRecord con startYear==1 y endYeard==1
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "periodRecord1", "title periodRecord test", "text periodRecord test", 1, 1, "http://asdf.com", null
			},

			/*
			 * A: Req1, 3.1
			 * B: Editar un periodRecord con title en blanco
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "periodRecord1", "", "text periodRecord test", 1966, 2000, "http://asdf.com", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1
			 * B: Editar un periodRecord con con text en blanco
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "periodRecord1", "title periodRecord test", "", 1966, 2000, "http://asdf.com", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1
			 * B: Editar un periodRecord con photo en blanco
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "periodRecord1", "title periodRecord test", "text periodRecord test", 1966, 2000, "", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1
			 * B: Editar un periodRecord con photo invalida
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "periodRecord1", "title periodRecord test", "text periodRecord test", 1966, 2000, "esto no es foto", IllegalArgumentException.class
			},
			/*
			 * A: Req1, 3.1
			 * B: Editar un legalRecord startYear negativo
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "periodRecord1", "title periodRecord test", "text periodRecord test", -1, 2000, "http://asdf.com", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1
			 * B: Editar un legalRecord endYear menor que starYear
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "periodRecord1", "title periodRecord test", "text periodRecord test", 2000, 21, "http://asdf.com", IllegalArgumentException.class
			},

			/*
			 * A: Req1, 3.1
			 * B: Editar un legalRecord que no es del principal
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "periodRecord3", "title periodRecord test", "text periodRecord test", 21, 21, "http://asdf.com", IllegalArgumentException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (String) testingData[i][3], (Integer) testingData[i][4], (Integer) testingData[i][5], (String) testingData[i][6],
				(Class<?>) testingData[i][7]);

	}
	protected void templateEdit(final String username, final Integer periodRecordId, final String title, final String text, final Integer startYear, final Integer endYear, final String photo, final Class<?> expected) {
		Class<?> caught;
		PeriodRecord periodRecord;
		PeriodRecord periodRecordSaved;

		this.startTransaction();

		caught = null;
		try {
			super.authenticate(username);

			periodRecord = this.periodRecordService.findOne(periodRecordId);

			periodRecord.setText(text);
			periodRecord.setTitle(title);
			periodRecord.setEndYear(endYear);
			periodRecord.setPhotos(photo);
			periodRecord.setStartYear(startYear);

			periodRecordSaved = this.periodRecordService.save(periodRecord);

			this.periodRecordService.flush();

			Assert.notNull(periodRecordSaved);

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

		int periodRecordId;
		PeriodRecord periodRecord;

		periodRecordId = super.getEntityId("periodRecord1");
		periodRecord = this.periodRecordService.findOne(periodRecordId);

		this.periodRecordService.delete(periodRecord);

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

		int periodRecordId;
		PeriodRecord periodRecord;

		periodRecordId = super.getEntityId("periodRecord3");
		periodRecord = this.periodRecordService.findOne(periodRecordId);

		this.periodRecordService.delete(periodRecord);

		super.unauthenticate();
	}

}
