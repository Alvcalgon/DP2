
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ParadeServiceTest extends AbstractTest {

	// The SUT --------------------------
	@Autowired
	private ParadeService	paradeService;


	// Other supporting services --------

	// Test

	/*
	 * A: Requirement tested: level B: requirement 22.2: The minimum, the maximum, the average
	 * and the standard deviation of the number of results in the finders.
	 * C: Analysis of sentence coverage: Sequence.
	 * D: Analysis of data coverage: intentionally blank
	 */
	@Test
	public void dataResultsPerFinder() {
		Double[] data;

		data = this.paradeService.findDataNumberResultsPerFinder();

		Assert.notNull(data);
		Assert.isTrue(data.length == 4);
	}

}
