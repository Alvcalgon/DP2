
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Customisation;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class CustomisationServiceTest extends AbstractTest {

	// Service under test ----------------------------------------
	@Autowired
	private CustomisationService	customisationService;


	// Supporting services --------------------------------------

	// Suite Tests ----------------------------------------------

	/*
	 * Test negativo: customisation es nulo.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negative_saveTest_uno() {
		super.authenticate("admin1");

		final Customisation customisation = null;
		Customisation saved;

		saved = this.customisationService.save(customisation);

		Assert.isNull(saved);

		super.unauthenticate();
	}

	/*
	 * Test negativo: se intenta almacenar una nueva instancia
	 * de la entidad Customisation.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void negative_saveTest_dos() {
		super.authenticate("admin1");

		Customisation customisation, saved;
		final String languages = "en,es";
		final String priorities = "LOW,MEDIUM";

		customisation = new Customisation();
		customisation.setName("Acme Testing");
		customisation.setBanner("https://tinyurl.com/acme-handy-worker-logo");
		customisation.setCountryCode("+34");
		customisation.setEnglishWelcomeMessage("Hello world");
		customisation.setSpanishWelcomeMessage("Hola mundo");
		customisation.setLanguages(languages);
		customisation.setMaxNumberResults(20);
		customisation.setTimeCachedResults(10);
		customisation.setPriorities(priorities);

		saved = this.customisationService.save(customisation);

		Assert.isNull(saved);

		super.unauthenticate();
	}

	/*
	 * Test positivo: se edita el objeto customisation
	 * persistido en la BD.
	 */
	@Test
	public void positive_saveTest_uno() {
		super.authenticate("admin1");

		Customisation find, saved;

		find = this.customisationService.find();

		find.setMaxNumberResults(50);
		find.setTimeCachedResults(20);

		saved = this.customisationService.save(find);

		Assert.notNull(saved);
		Assert.isTrue(saved.getId() != 0);

		super.unauthenticate();
	}
}
