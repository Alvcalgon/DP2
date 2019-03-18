
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
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede crear periodRecord
			 * C: 80%(16/20) Recorre 16 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 11 atributos, 2 pueden tomar valores en blanco o no, 1 en blanco o no y no cumplir el formato y dos pueden ser mayor o menor a 1
			 */
			{
				"brotherhood1", "title periodRecord test", "text periodRecord test", 1966, 2000, "http://asdf.com", null
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede crear periodRecord
			 * C: 80%(16/20) Recorre 16 lineas de código de las 20 posibles
			 * D:18%, (2/11) hay 11 atributos, 2 pueden tomar valores en blanco o no, 1 en blanco o no y no cumplir el formato y dos pueden ser mayor o menor a 1
			 */
			{
				"brotherhood1", "title periodRecord test", "text periodRecord test", 1966, 2000, "http://asdf.com", null
			},

			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede crear periodRecord
			 * B: Crear un periodRecord sin título
			 * C: 80%(16/20) Recorre 16 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 11 atributos, 2 pueden tomar valores en blanco o no, 1 en blanco o no y no cumplir el formato y dos pueden ser mayor o menor a 1
			 */
			{
				"brotherhood1", "", "text periodRecord test", 1966, 2000, "http://asdf.com", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede crear periodRecord
			 * B: Crear un periodRecord sin texto
			 * C: 80%(16/20) Recorre 16 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 11 atributos, 2 pueden tomar valores en blanco o no, 1 en blanco o no y no cumplir el formato y dos pueden ser mayor o menor a 1
			 */
			{
				"brotherhood1", "title periodRecord test", "", 1966, 2000, "http://asdf.com", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede crear periodRecord
			 * B: Crear un periodRecord sin photo
			 * C: 80%(16/20) Recorre 16 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 11 atributos, 2 pueden tomar valores en blanco o no, 1 en blanco o no y no cumplir el formato y dos pueden ser mayor o menor a 1
			 */
			{
				"brotherhood1", "title periodRecord test", "text periodRecord test", 1966, 2000, "", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede crear periodRecord
			 * B: Crear un periodRecord sin seguir el formato url para las fotos
			 * C: 80%(16/20) Recorre 16 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 11 atributos, 2 pueden tomar valores en blanco o no, 1 en blanco o no y no cumplir el formato y dos pueden ser mayor o menor a 1
			 */
			{
				"brotherhood1", "title periodRecord test", "text periodRecord test", 1966, 2000, "esto no es foto", IllegalArgumentException.class
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede crear periodRecord
			 * B: Crear un periodRecord startYear ==0
			 * C: 80%(16/20) Recorre 16 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 11 atributos, 2 pueden tomar valores en blanco o no, 1 en blanco o no y no cumplir el formato y dos pueden ser mayor o menor a 1
			 */
			{
				"brotherhood1", "title periodRecord test", "text periodRecord test", 0, 2000, "http://asdf.com", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede crear periodRecord
			 * B: Crear un periodRecord startYear posterior a endYear
			 * C: 45%(9/20) Recorre 9 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 11 atributos, 2 pueden tomar valores en blanco o no, 1 en blanco o no y no cumplir el formato y dos pueden ser mayor o menor a 1
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
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede editar sus periodRecord
			 * C: 85%(17/20) Recorre 17 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 11 atributos, 2 pueden tomar valores en blanco o no, 1 en blanco o no y no cumplir el formato y dos pueden ser mayor o menor a 1
			 */
			{
				"brotherhood1", "periodRecord1", "title periodRecord test", "text periodRecord test", 1966, 2000, "http://asdf.com", null
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede editar periodRecord
			 * C: 85%(17/20) Recorre 17 lineas de código de las 20 posibles
			 * D:18%, (2/11) hay 11 atributos, 2 pueden tomar valores en blanco o no, 1 en blanco o no y no cumplir el formato y dos pueden ser mayor o menor a 1
			 */
			{
				"brotherhood1", "periodRecord1", "title periodRecord test", "text periodRecord test", 1, 1, "http://asdf.com", null
			},

			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede crear periodRecord
			 * B: Editar un periodRecord sin título
			 * C: 85%(17/20) Recorre 17 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 11 atributos, 2 pueden tomar valores en blanco o no, 1 en blanco o no y no cumplir el formato y dos pueden ser mayor o menor a 1
			 */
			{
				"brotherhood1", "periodRecord1", "", "text periodRecord test", 1966, 2000, "http://asdf.com", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede crear periodRecord
			 * B: Editar un periodRecord sin texto
			 * C: 85%(17/20) Recorre 17 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 11 atributos, 2 pueden tomar valores en blanco o no, 1 en blanco o no y no cumplir el formato y dos pueden ser mayor o menor a 1
			 */
			{
				"brotherhood1", "periodRecord1", "title periodRecord test", "", 1966, 2000, "http://asdf.com", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede crear periodRecord
			 * B: Editar un periodRecord sin photo
			 * C: 85%(17/20) Recorre 17 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 11 atributos, 2 pueden tomar valores en blanco o no, 1 en blanco o no y no cumplir el formato y dos pueden ser mayor o menor a 1
			 */
			{
				"brotherhood1", "periodRecord1", "title periodRecord test", "text periodRecord test", 1966, 2000, "", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede crear periodRecord
			 * B: Editar un periodRecord sin seguir el patron url para las fotos
			 * C: 85%(17/20) Recorre 17 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 11 atributos, 2 pueden tomar valores en blanco o no, 1 en blanco o no y no cumplir el formato y dos pueden ser mayor o menor a 1
			 */
			{
				"brotherhood1", "periodRecord1", "title periodRecord test", "text periodRecord test", 1966, 2000, "esto no es foto", IllegalArgumentException.class
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede crear periodRecord
			 * B: Editar un periodRecord con starYeard negativo
			 * C: 85%(1/20) Recorre 17 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 11 atributos, 2 pueden tomar valores en blanco o no, 1 en blanco o no y no cumplir el formato y dos pueden ser mayor o menor a 1
			 */
			{
				"brotherhood1", "periodRecord1", "title periodRecord test", "text periodRecord test", -1, 2000, "http://asdf.com", ConstraintViolationException.class
			},
			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede crear periodRecord
			 * B: Editar un periodRecord con starYear posterior a endYear
			 * C: 45%(9/20) Recorre 9 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 11 atributos, 2 pueden tomar valores en blanco o no, 1 en blanco o no y no cumplir el formato y dos pueden ser mayor o menor a 1
			 */
			{
				"brotherhood1", "periodRecord1", "title periodRecord test", "text periodRecord test", 2000, 21, "http://asdf.com", IllegalArgumentException.class
			},

			/*
			 * A: Req1, 3.1: Un actor autenticado como brotherhod puede crear periodRecord
			 * B: Editar un periodRecord de otro usuario
			 * C: 80%(16/20) Recorre 16 lineas de código de las 20 posibles
			 * D:9%, (1/11) hay 11 atributos, 2 pueden tomar valores en blanco o no, 1 en blanco o no y no cumplir el formato y dos pueden ser mayor o menor a 1
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
	 * A: Req1, 3.1: Un actor autenticado como brotherhod puede borrar sus periodRecords
	 * C: 100%(11/11) Recorre 11 lineas de código de las 11 posibles
	 * D:Intencionadamente en blanco; no hay datos que checkear
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
	 * A: Req1, 3.1: Un actor autenticado como brotherhod puede borrar sus periodRecords
	 * B: Un usuario borrar el periodRecord de otro usuario
	 * C: 54.5%(6/11) Recorre 6 lineas de código de las 11 posibles
	 * D:Intencionadamente en blanco; no hay datos que checkear
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

	/*
	 * A: Req1, 3.1: Un actor autenticado como brotherhod puede mostrar sus periodRecords
	 * C: 100%(5/5) Recorre 5 lineas de código de las 5 posibles
	 * D:Intencionadamente en blanco; no hay datos que checkear
	 */
	@Test
	public void displayAuthenticated_positive_test() {
		super.authenticate("member2");

		final int periodRecordId;
		PeriodRecord periodRecord;

		periodRecordId = super.getEntityId("periodRecord1");
		periodRecord = this.periodRecordService.findOne(periodRecordId);

		Assert.notNull(periodRecord);

		super.unauthenticate();
	}

	/*
	 * A: Req2.1: Un actor no autenticado puede mostrar periodRecords de las historias de los brotherhood
	 * C: 100%(5/5) Recorre 5 lineas de código de las 5 posibles
	 * D: Intencionadamente en blanco; no hay datos que checkear
	 */
	@Test
	public void displayNotAuthenticated_positive_test() {

		final int periodRecordId;
		final PeriodRecord periodRecord;

		periodRecordId = super.getEntityId("periodRecord1");
		periodRecord = this.periodRecordService.findOne(periodRecordId);

		Assert.notNull(periodRecord);

	}

}
