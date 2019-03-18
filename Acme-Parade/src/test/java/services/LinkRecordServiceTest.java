
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
			 * A: Req1, 3.1
			 * B: Crea adecuadamente un linkRecord
			 * C:
			 * D:
			 */
			{
				"brotherhood1", " title linkRecord", "text linkRecord", "brotherhood2", null
			},
			/*
			 * A: Req1, 3.1
			 * B: Crear un linkRecord cuyo link a brotherhood es él mismo
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "title linkRecord testd", "text linkRecord test", "brotherhood1", IllegalArgumentException.class
			},
			/*
			 * A: Req1, 3.1
			 * B: Crear un linkRecord con title en blanco
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "", "text linkRecord test", "brotherhood2", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1
			 * B: Crear un linkRecord cuyo link con texto en blanco
			 * C:
			 * D:
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
			 * A: Req1, 3.1
			 * B: Editar adecuadamente un linkRecord
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "linkRecord2", "linkRecord test", "linkRecord test", "brotherhood2", null
			},
			/*
			 * A: Req1, 3.1
			 * B: Editar un linkRecord cuyo link a brotherhood es él mismo
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "linkRecord2", "linkRecord test", "linkRecord test", "brotherhood1", IllegalArgumentException.class
			},
			/*
			 * A: Req1, 3.1
			 * B: Editar un linkRecord con title en blanco
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "linkRecord2", "", "linkRecord test", "brotherhood2", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1
			 * B: Editar un linkRecord cuyo link con texto en blanco
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "linkRecord2", "inceptionRecordTest1", "", "brotherhood2", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1
			 * B: Editar un linkRecord que no es suyo
			 * C:
			 * D:
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
	 * A: Req1, 3.1
	 * B:Borrar
	 * C:
	 * D:
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
	 * A: Req1, 3.1
	 * B:Borrar uno que no es suyo
	 * C:
	 * D:
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
	 * A: Req1, 3.1
	 * B:Display autenticado
	 * C:
	 * D:
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
	 * A: Req2.1
	 * B:Display sin autenticar
	 * C:
	 * D:
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
