
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
import domain.InceptionRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class InceptionServiceTest extends AbstractTest {

	// Services under test --------------------------

	@Autowired
	private InceptionRecordService	inceptionRecordService;


	// Other supporting services --------------------

	// Suite test -----------------------------------

	//	@Test
	//	public void driverCreate() {
	//		final Object testingData[][] = {
	//			/*
	//			 * A: Req1, 3.1
	//			 * B: Crea adecuadamente un inceptionRecord (historia)
	//			 * C:
	//			 * D:
	//			 */
	//			{
	//				"brotherhood4", "inceptionRecordTest1", "textInceptionRecordTest1", "http://photo.com", null
	//			},
	//			/*
	//			 * A: Req1, 3.1
	//			 * B: Crear un inceptionRecord (historia) con title blanco
	//			 * C:
	//			 * D:
	//			 */
	//			{
	//				"brotherhood4", "", "text inceptionRecord test", "http://photo.com", ConstraintViolationException.class
	//			},
	//			/*
	//			 * A: Req1, 3.1
	//			 * B: Crear un inceptionRecord (historia) con texto blanco
	//			 * C:
	//			 * D:
	//			 */
	//			{
	//				"brotherhood4", "title inceptionRecord test", "", "http://photo.com", ConstraintViolationException.class
	//			},
	//			/*
	//			 * A: Req1, 3.1
	//			 * B: Crear un inceptionRecord (historia) con photo blanco
	//			 * C:
	//			 * D:
	//			 */
	//			{
	//				"brotherhood4", "title inceptionRecord test", "text inceptionRecord test", "", ConstraintViolationException.class
	//			},
	//			/*
	//			 * A: Req1, 3.1
	//			 * B: Crear un inceptionRecord (historia) sin formato del link
	//			 * C:
	//			 * D:
	//			 */
	//			{
	//				"brotherhood4", "title inceptionRecord test", "text inceptionRecord test", "foto sin formato", IllegalArgumentException.class
	//			},
	//
	//		};
	//
	//		for (int i = 0; i < testingData.length; i++)
	//			this.templateCreate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	//
	//	}
	//
	//	protected void templateCreate(final String username, final String titleInception, final String textInception, final String photoInception, final Class<?> expected) {
	//		Class<?> caught;
	//		InceptionRecord inceptionRecord;
	//		InceptionRecord inceptionRecordSaved;
	//
	//		this.startTransaction();
	//
	//		caught = null;
	//		try {
	//			super.authenticate(username);
	//
	//			inceptionRecord = this.inceptionRecordService.create();
	//
	//			inceptionRecord.setTitle(titleInception);
	//			inceptionRecord.setText(textInception);
	//			inceptionRecord.setPhotos(photoInception);
	//
	//			inceptionRecordSaved = this.inceptionRecordService.save(inceptionRecord);
	//			this.inceptionRecordService.flush();
	//
	//			Assert.notNull(inceptionRecordSaved);
	//			Assert.isTrue(inceptionRecordSaved.getId() != 0);
	//
	//			super.unauthenticate();
	//		} catch (final Throwable oops) {
	//			caught = oops.getClass();
	//		}
	//
	//		this.rollbackTransaction();
	//
	//		super.checkExceptions(expected, caught);
	//	}
	@Test
	public void driverEdit() {
		final Object testingData[][] = {
			/*
			 * A: Req1, 3.1
			 * B: Editar adecuadamente un inceptionRecord (historia)
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "inceptionRecord1", "inceptionRecordTest1", "textInceptionRecordTest1", "http://photo.com", null
			},
			/*
			 * A: Req1, 3.1
			 * B: Editar un inceptionRecord (historia) con title blanco
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "inceptionRecord1", "", "text inceptionRecord test", "http://photo.com", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1
			 * B: Editar un inceptionRecord (historia) con texto blanco
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "inceptionRecord1", "title inceptionRecord test", "", "http://photo.com", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1
			 * B: Editar un inceptionRecord (historia) con photo blanco
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "inceptionRecord1", "title inceptionRecord test", "text inceptionRecord test", "", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1
			 * B: Editar un inceptionRecord (historia) sin formato del link
			 * C:
			 * D:
			 */
			{
				"brotherhood1", "inceptionRecord1", "title inceptionRecord test", "text inceptionRecord test", "foto sin formato", IllegalArgumentException.class
			},
			/*
			 * A: Req1, 3.1
			 * B: Editar un inceptionRecord que no es del principal
			 * C:
			 * D:
			 */
			{
				"brotherhood2", "inceptionRecord1", "title inceptionRecord test", "text inceptionRecord test", "http://asdf.com", IllegalArgumentException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);

	}

	protected void templateEdit(final String username, final Integer inceptionRecordId, final String titleInception, final String textInception, final String photoInception, final Class<?> expected) {
		Class<?> caught;
		InceptionRecord inceptionRecord;
		InceptionRecord inceptionRecordSaved;

		this.startTransaction();

		caught = null;
		try {
			super.authenticate(username);

			inceptionRecord = this.inceptionRecordService.findOne(inceptionRecordId);

			inceptionRecord.setTitle(titleInception);
			inceptionRecord.setText(textInception);
			inceptionRecord.setPhotos(photoInception);

			inceptionRecordSaved = this.inceptionRecordService.save(inceptionRecord);
			this.inceptionRecordService.flush();

			Assert.notNull(inceptionRecordSaved);
			Assert.isTrue(inceptionRecordSaved.getId() != 0);

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

}
