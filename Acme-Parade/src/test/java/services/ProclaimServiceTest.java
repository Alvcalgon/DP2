
package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Proclaim;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ProclaimServiceTest extends AbstractTest {

	// The SUT -------------------
	@Autowired
	private ProclaimService	proclaimService;


	// Other supporting test ------

	// Test -----------------------

	/*
	 * A: Requirement tested: level A: requirement 14.2: browse the proclaims
	 * of the chapter.
	 * C: Analysis of sentence coverage: Sequence.
	 * D: Analysis of data coverage: intentionally blank.
	 */
	@Test
	public void findAll_test() {
		Collection<Proclaim> all;

		all = this.proclaimService.findAll();

		Assert.notNull(all);
		Assert.isTrue(!all.isEmpty());
	}

	/*
	 * A: Req 12: Chapters can publish proclaims. For every proclaim, the system must store the moment when its published and a piece of text that cant be longer than 250 characters.
	 * C: 100%(11/11) Recorre 11 lineas de código de las 11 posibles
	 * D:Intencionadamente en blanco; no hay datos que checkear
	 */
	@Test
	public void create_positive_test() {
		super.authenticate("chapter1");

		Proclaim proclaim;

		proclaim = this.proclaimService.create();

		proclaim.setText("Este es un texto de prueba");

		this.proclaimService.save(proclaim);

		super.unauthenticate();
	}

	/*
	 * A: Req 12: Chapters can publish proclaims. For every proclaim, the system must store the moment when its published and a piece of text that cant be longer than 250 characters.
	 * B: Insertamos una fecha que no sea past
	 * C: 54.5%(6/11) Recorre 6 lineas de código de las 11 posibles
	 * D:Intencionadamente en blanco; no hay datos que checkear
	 */
	@Test(expected = IllegalArgumentException.class)
	public void create_negative_test() {
		super.authenticate("brotherhood1");

		Proclaim proclaim;

		proclaim = this.proclaimService.create();

		proclaim
			.setText("vamos a escribir 252 caracteres, vamos a escribir 252 caracteres, vamos a escribir 252 caracteres, vamos a escribir 252 caracteres, vamos a escribir 252 caracteres, vamos a escribir 252 caracteres, vamos a escribir 252 caracteres, vamos a escribir 252 ,para probarlo");

		this.proclaimService.save(proclaim);

		super.unauthenticate();
	}

	/*
	 * A: Req 17.1: Publish a proclaim. Note that once a proclaim is published, theres no way to update or delete it, so double confirmation prior to publication is a must.
	 * B: El proclaim no se puede editar
	 * C: 100%(11/11) Recorre 11 lineas de código de las 11 posibles
	 * D:Intencionadamente en blanco; no hay datos que checkear
	 */
	@Test(expected = IllegalArgumentException.class)
	public void edit_negative_test() {
		super.authenticate("chapter1");

		int proclaimId;
		Proclaim proclaim;

		proclaimId = super.getEntityId("proclaim1");
		proclaim = this.proclaimService.findOne(proclaimId);

		proclaim.setText("Nuevo texto");

		this.proclaimService.save(proclaim);

		super.unauthenticate();
	}

}
