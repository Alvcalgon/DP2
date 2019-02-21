
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Box;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class BoxServiceTest extends AbstractTest {

	// Service under testing -----------------
	@Autowired
	private BoxService	boxService;


	// Other services ------------------------

	// Suite test ----------------------------
	@Test
	public void testCreate() {
		super.authenticate("member2");

		Box box;

		box = this.boxService.create();

		Assert.notNull(box);
		Assert.notNull(box.getActor());
		Assert.notNull(box.getMessages());
		Assert.isNull(box.getName());
		Assert.isNull(box.getParent());

		super.unauthenticate();
	}

}
