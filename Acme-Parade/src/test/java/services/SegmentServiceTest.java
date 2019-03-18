
package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Segment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class SegmentServiceTest extends AbstractTest {

	// The SUT --------------------------

	@Autowired
	private SegmentService	segmentService;


	// Supporting services --------------

	// Tests ------------------------

	/**
	 * Requirement tested: Level B - requirement 3.3.
	 * Brotherhood creates a segment, when it's creates their attributes are null.
	 * Analysis of sentence coverage: Sequence
	 * Analysis of data coverage: Intentionally blank
	 */
	@Test
	public void create_test() {
		super.authenticate("brotherhood1");

		Segment segment;

		segment = this.segmentService.create();

		Assert.isNull(segment.getOrigin());
		Assert.isNull(segment.getDestination());
		Assert.isNull(segment.getReachingOrigin());
		Assert.isNull(segment.getReachingDestination());

		super.unauthenticate();

	}

}
