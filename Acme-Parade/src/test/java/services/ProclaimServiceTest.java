
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

}
