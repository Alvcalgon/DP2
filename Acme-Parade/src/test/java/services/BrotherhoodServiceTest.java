
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Brotherhood;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class BrotherhoodServiceTest extends AbstractTest {

	// Services under testing --------------------------------
	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Supporting test ---------------------------------------

	// Test

	@Test
	public void testBrotherhood() {
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.create();

		Assert.notNull(brotherhood);
		Assert.notNull(brotherhood.getUserAccount());
		Assert.isNull(brotherhood.getAddress());
		Assert.isNull(brotherhood.getEmail());
		Assert.isNull(brotherhood.getMiddleName());
		Assert.isNull(brotherhood.getName());
		Assert.isNull(brotherhood.getPhoneNumber());
		Assert.isNull(brotherhood.getPhoto());
		Assert.isNull(brotherhood.getSurname());
		Assert.isNull(brotherhood.getArea());
		Assert.isNull(brotherhood.getEstablishmentDate());
		Assert.isNull(brotherhood.getTitle());
		Assert.isNull(brotherhood.getPictures());

		Assert.isTrue(brotherhood.getIsSpammer() == false);
		Assert.isNull(brotherhood.getScore());
	}

	@Test
	public void testCreate_dos() {
		Brotherhood brotherhood, saved;

		brotherhood = this.brotherhoodService.create();

		brotherhood.setEmail("broth@mail.com");
		brotherhood.setName("brother1");
		brotherhood.setSurname("brother1");
		brotherhood.setTitle("TEST");

		saved = this.brotherhoodService.save(brotherhood);

		Assert.isTrue(this.brotherhoodService.findOne(saved.getId()) != null);
	}
}
