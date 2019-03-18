
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

	/*
	 * REQUIREMENT TESTED: An actor who is authenticated as an administrator
	 * must be able to display a dashboard with the following information:
	 * The average, the minimum, the maximum and the standard deviation of
	 * the number of parades co-ordinated by the chapter.
	 * 
	 * ANALISYS OF SENTENCE COVERAGE: 100% of sentence coverage.
	 * 
	 * ANALISYS OF DATA COVERAGE: 100% of data coverage.
	 */
	@Test
	public void testDataNumerParadesCoordinatedByChapters() {
		Double[] data;

		data = this.paradeService.findDataNumerParadesCoordinatedByChapters();

		Assert.isTrue(data[0] == 8.0);
		Assert.isTrue(data[1] == 5.0);
		Assert.isTrue(data[2] == 11.0);
		Assert.isTrue(data[3] == 3.0);
	}

	/*
	 * REQUIREMENT TESTED: An actor who is authenticated as an administrator
	 * must be able to display a dashboard with the following information:
	 * The ratio of parades in draft mode vs parades y final mode.
	 * 
	 * ANALISYS OF SENTENCE COVERAGE: 100% of sentence coverage.
	 * 
	 * ANALISYS OF DATA COVERAGE: 100% of data coverage.
	 */
	@Test
	public void testRatioParadesDraftModeVSParadesFinalMode() {
		Double ratio;

		ratio = this.paradeService.findRatioParadesDraftModeVSParadesFinalMode();

		Assert.isTrue(ratio == 0.11111);
	}

	/*
	 * REQUIREMENT TESTED: An actor who is authenticated as an administrator
	 * must be able to display a dashboard with the following information:
	 * The ratio of parades in final mode grouped by status. [SUBMITTED]
	 * 
	 * ANALISYS OF SENTENCE COVERAGE: 100% of sentence coverage.
	 * 
	 * ANALISYS OF DATA COVERAGE: 100% of data coverage.
	 */
	@Test
	public void testRatioSubmittedParadesFinalMode() {
		Double ratio;

		ratio = this.paradeService.findRatioSubmittedParadesFinalMode();

		Assert.isTrue(ratio == 0.11111);
	}

	/*
	 * REQUIREMENT TESTED: An actor who is authenticated as an administrator
	 * must be able to display a dashboard with the following information:
	 * The ratio of parades in final mode grouped by status. [ACCEPTED]
	 * 
	 * ANALISYS OF SENTENCE COVERAGE: 100% of sentence coverage.
	 * 
	 * ANALISYS OF DATA COVERAGE: 100% of data coverage.
	 */
	@Test
	public void testRatioAcceptedParadesFinalMode() {
		Double ratio;

		ratio = this.paradeService.findRatioAcceptedParadesFinalMode();

		Assert.isTrue(ratio == 0.77778);
	}

	/*
	 * REQUIREMENT TESTED: An actor who is authenticated as an administrator
	 * must be able to display a dashboard with the following information:
	 * The ratio of parades in final mode grouped by status. [REJECTED]
	 * 
	 * ANALISYS OF SENTENCE COVERAGE: 100% of sentence coverage.
	 * 
	 * ANALISYS OF DATA COVERAGE: 100% of data coverage.
	 */
	@Test
	public void testRatioRejectedParadesFinalMode() {
		Double ratio;

		ratio = this.paradeService.findRatioRejectedParadesFinalMode();

		Assert.isTrue(ratio == 0.11111);
	}

}
