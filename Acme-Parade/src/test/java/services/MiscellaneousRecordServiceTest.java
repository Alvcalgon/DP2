
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
import domain.MiscellaneousRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class MiscellaneousRecordServiceTest extends AbstractTest {

	// Services under test --------------------------

	@Autowired
	private MiscellaneousRecordService	miscellaneousRecordService;


	// Other supporting services --------------------

	// Suite test -----------------------------------

	@Test
	public void driverCreate() {
		final Object testingData[][] = {
			/*
			 * A: Req1, 3.1
			 * B: Crea adecuadamente un MiscellaneousRecord
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "title MiscellaneousRecord test", "text MiscellaneousRecord test", null
			},

			/*
			 * A: Req1, 3.1
			 * B: Crear un MiscellaneousRecord con title en blanco
			 * C:
			 * D:
			 */
			{
				"brotherhood1", " ", "text MiscellaneousRecord test", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1
			 * B: Crear un MiscellaneousRecord con con text en blanco
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "title MiscellaneousRecord test", "", ConstraintViolationException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	protected void templateCreate(final String username, final String title, final String text, final Class<?> expected) {
		Class<?> caught;
		final MiscellaneousRecord miscellaneousRecord;
		final MiscellaneousRecord miscellaneousRecordSaved;

		this.startTransaction();

		caught = null;
		try {
			super.authenticate(username);

			miscellaneousRecord = this.miscellaneousRecordService.create();

			miscellaneousRecord.setText(text);
			miscellaneousRecord.setTitle(title);

			miscellaneousRecordSaved = this.miscellaneousRecordService.save(miscellaneousRecord);

			this.miscellaneousRecordService.flush();

			Assert.notNull(miscellaneousRecordSaved);

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
			 * B: Editar adecuadamente un MiscellaneousRecord
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "miscellaneousRecord1", "title MiscellaneousRecord test", "text MiscellaneousRecord test", null
			},

			/*
			 * A: Req1, 3.1
			 * B: Editar un MiscellaneousRecord con title en blanco
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "miscellaneousRecord1", " ", "text MiscellaneousRecord test", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1
			 * B: Editar un MiscellaneousRecord con con text en blanco
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "miscellaneousRecord1", "title MiscellaneousRecord test", "", ConstraintViolationException.class
			},

			/*
			 * A: Req1, 3.1
			 * B: Editar un MiscellaneousRecord que no es del principal
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "miscellaneousRecord3", "title MiscellaneousRecord test", "text MiscellaneousRecord test", IllegalArgumentException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);

	}
	protected void templateEdit(final String username, final Integer miscellaneousRecordId, final String title, final String text, final Class<?> expected) {
		Class<?> caught;
		MiscellaneousRecord miscellaneousRecord;
		MiscellaneousRecord miscellaneousRecordSaved;

		this.startTransaction();

		caught = null;
		try {
			super.authenticate(username);

			miscellaneousRecord = this.miscellaneousRecordService.findOneEdit(miscellaneousRecordId);

			miscellaneousRecord.setText(text);
			miscellaneousRecord.setTitle(title);

			miscellaneousRecordSaved = this.miscellaneousRecordService.save(miscellaneousRecord);

			this.miscellaneousRecordService.flush();

			Assert.notNull(miscellaneousRecordSaved);

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

		int miscellaneousRecordId;
		MiscellaneousRecord miscellaneousRecord;

		miscellaneousRecordId = super.getEntityId("miscellaneousRecord1");
		miscellaneousRecord = this.miscellaneousRecordService.findOne(miscellaneousRecordId);

		this.miscellaneousRecordService.delete(miscellaneousRecord);

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

		int miscellaneousRecordId;
		MiscellaneousRecord miscellaneousRecord;

		miscellaneousRecordId = super.getEntityId("miscellaneousRecord3");
		miscellaneousRecord = this.miscellaneousRecordService.findOne(miscellaneousRecordId);

		this.miscellaneousRecordService.delete(miscellaneousRecord);

		super.unauthenticate();
	}

	/*
	 * A: Req1, 3.1
	 * B:Display autenticado
	 * C:
	 * D:
	 */
	@Test
	public void displayAuthenticated_positive_test() {
		super.authenticate("member2");

		final int miscellaneousRecordId;
		MiscellaneousRecord miscellaneousRecord;

		miscellaneousRecordId = super.getEntityId("miscellaneousRecord1");
		miscellaneousRecord = this.miscellaneousRecordService.findOne(miscellaneousRecordId);

		Assert.notNull(miscellaneousRecord);

		super.unauthenticate();
	}

	/*
	 * A: Req2.1
	 * B:Display sin autenticar
	 * C:
	 * D:
	 */
	@Test
	public void displayNotAuthenticated_positive_test() {

		final int miscellaneousRecordId;
		MiscellaneousRecord miscellaneousRecord;

		miscellaneousRecordId = super.getEntityId("miscellaneousRecord1");
		miscellaneousRecord = this.miscellaneousRecordService.findOne(miscellaneousRecordId);

		Assert.notNull(miscellaneousRecord);

	}
}
