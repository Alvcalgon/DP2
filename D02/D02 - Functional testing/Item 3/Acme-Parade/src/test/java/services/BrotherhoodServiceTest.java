
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
import domain.Brotherhood;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class BrotherhoodServiceTest extends AbstractTest {

	// Services under testing (SUT) --------------------------------
	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Supporting services ---------------------------------------

	// Test ----------------------------------------------------
	/*
	 * A: Requirement tested: level C: requirement 12.3: The largest brotherhoods
	 * C: Analysis of sentence coverage: Sequence.
	 * D: Analysis of data coverage: intentionally blank
	 */
	@Test
	public void largestBrotherhoods_test() {
		Collection<Brotherhood> largest_brotherhoods;

		largest_brotherhoods = this.brotherhoodService.findLargest();

		Assert.notNull(largest_brotherhoods);
		Assert.isTrue(largest_brotherhoods.size() > 0);
	}

	/*
	 * A: Requirement tested: level C: requirement 12.3: The smallest brotherhoods
	 * C: Analysis of sentence coverage: Sequence.
	 * D: Analysis of data coverage: intentionally blank
	 */
	@Test
	public void smallestBrotherhoods_test() {
		Collection<Brotherhood> smallest_brotherhoods;

		smallest_brotherhoods = this.brotherhoodService.findSmallest();

		Assert.notNull(smallest_brotherhoods);
		Assert.isTrue(smallest_brotherhoods.size() > 0);
	}

}
