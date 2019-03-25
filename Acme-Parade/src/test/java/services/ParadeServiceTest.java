
package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Brotherhood;
import domain.Chapter;
import domain.Parade;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ParadeServiceTest extends AbstractTest {

	// The SUT --------------------------
	@Autowired
	private ParadeService		paradeService;

	// Other supporting services --------

	@Autowired
	private ChapterService		chapterService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


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
	 * A: An actor who is authenticated as an administrator must be able to
	 * display a dashboard with the following information:
	 * The average, the minimum, the maximum and the standard deviation of
	 * the number of parades co-ordinated by the chapter.
	 * 
	 * B: Positive test
	 * 
	 * C: 100% of sentence coverage.
	 * 
	 * D: 100% of data coverage.
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
	 * A: An actor who is authenticated as an administrator must be able to
	 * display a dashboard with the following information:
	 * The ratio of parades in draft mode vs parades y final mode.
	 * 
	 * B: Positive test
	 * 
	 * C: 100% of sentence coverage.
	 * 
	 * D: 100% of data coverage.
	 */
	@Test
	public void testRatioParadesDraftModeVSParadesFinalMode() {
		Double ratio;

		ratio = this.paradeService.findRatioParadesDraftModeVSParadesFinalMode();

		Assert.isTrue(ratio == 0.11111);
	}

	/*
	 * A: An actor who is authenticated as an administrator must be able to
	 * display a dashboard with the following information:
	 * The ratio of parades in final mode grouped by status. [SUBMITTED]
	 * 
	 * B: Positive test
	 * 
	 * C: 100% of sentence coverage.
	 * 
	 * D: 100% of data coverage.
	 */
	@Test
	public void testRatioSubmittedParadesFinalMode() {
		Double ratio;

		ratio = this.paradeService.findRatioSubmittedParadesFinalMode();

		Assert.isTrue(ratio == 0.11111);
	}

	/*
	 * A: An actor who is authenticated as an administrator must be able to
	 * display a dashboard with the following information:
	 * The ratio of parades in final mode grouped by status. [ACCEPTED]
	 * 
	 * B: Positive test
	 * 
	 * C: 100% of sentence coverage.
	 * 
	 * D: 100% of data coverage.
	 */
	@Test
	public void testRatioAcceptedParadesFinalMode() {
		Double ratio;

		ratio = this.paradeService.findRatioAcceptedParadesFinalMode();

		Assert.isTrue(ratio == 0.77778);
	}

	/*
	 * A: An actor who is authenticated as an administrator must be able to
	 * display a dashboard with the following information:
	 * The ratio of parades in final mode grouped by status. [REJECTED]
	 * 
	 * B: Positive test
	 * 
	 * C: 100% of sentence coverage.
	 * 
	 * D: 100% of data coverage.
	 */
	@Test
	public void testRatioRejectedParadesFinalMode() {
		Double ratio;

		ratio = this.paradeService.findRatioRejectedParadesFinalMode();

		Assert.isTrue(ratio == 0.11111);
	}

	/*
	 * A: A chapter logged list published parades grouped by status of a brotherhood in the area that he/she coordinates.
	 * C: 100% of sentence coverage
	 * D: 100% of data coverage
	 */
	@Test
	public void list_publishedParadesGroupedByStatusChapter_positive_test() {
		super.authenticate("chapter1");
		Chapter chapter;

		chapter = this.chapterService.findOne(super.getEntityId("chapter1"));
		Collection<Parade> paradesSubmitted, paradesAccepted, paradesRejected;

		paradesSubmitted = this.paradeService.findSubmittedByArea(chapter.getArea().getId());
		paradesAccepted = this.paradeService.findAcceptedByArea(chapter.getArea().getId());
		paradesRejected = this.paradeService.findRejectedByArea(chapter.getArea().getId());

		Assert.notNull(paradesSubmitted);
		Assert.notNull(paradesAccepted);
		Assert.notNull(paradesRejected);

		super.unauthenticate();

	}

	/*
	 * A: Chapter try to list the parades published
	 * B: A chapter try to list the parades published of an area different of his/her.
	 * C: Approximately 77% of sentence coverage, since it has been covered 7 lines of code of 9 possible.
	 * D: 100% of data coverage
	 */
	@Test(expected = IllegalArgumentException.class)
	public void list_publishedParadesGroupedByStatusChapter_negative_test() {
		super.authenticate("chapter2");
		Chapter chapter;

		chapter = this.chapterService.findOne(super.getEntityId("chapter1"));
		Collection<Parade> paradesSubmitted, paradesAccepted, paradesRejected;

		paradesSubmitted = this.paradeService.findSubmittedByArea(chapter.getArea().getId());
		paradesAccepted = this.paradeService.findAcceptedByArea(chapter.getArea().getId());
		paradesRejected = this.paradeService.findRejectedByArea(chapter.getArea().getId());

		Assert.notNull(paradesSubmitted);
		Assert.notNull(paradesAccepted);
		Assert.notNull(paradesRejected);

		super.unauthenticate();

	}

	/*
	 * A: Chapter rejected a parade writing a rejected reason.
	 * C: 100% of sentence coverage
	 * D: 33% (1/3) of data coverage for this test. Parade::reasonWhy could be blank, null or could have a valid value.
	 */
	@Test
	public void reject_parade_positive_test() {
		super.authenticate("chapter2");

		Parade parade;

		parade = this.paradeService.findOne(super.getEntityId("parade3"));
		parade.setReasonWhy("It could not be possible");

		this.paradeService.saveRejected(parade);

		super.unauthenticate();

	}

	/*
	 * A: Chapter rejects a parade
	 * B: Chapter tries to reject a parade without writes a reason why.
	 * C: Approximately 71% of sentence coverage,since it has been covered 5 lines of code of 7 possible.
	 * D: 33% (1/3) of data coverage for this test. Parade::reasonWhy could be blank, null or could have a valid value.
	 */
	@Test(expected = DataIntegrityViolationException.class)
	public void reject_parade_negative_test() {
		super.authenticate("chapter2");

		Parade parade;

		parade = this.paradeService.findOne(super.getEntityId("parade3"));

		this.paradeService.saveRejected(parade);

		super.unauthenticate();
	}

	/*
	 * A:Brotherhood list their parades grouped by status
	 * C: 100% of sentence coverage
	 * D: 100% of data coverage
	 */
	@Test
	public void list_publishedParadesGroupedByStatusBrotherhood_positive_test() {
		super.authenticate("brotherhood1");

		Collection<Parade> paradesSubmittedFinal, paradesRejectedFinal, paradesAcceptedFinal;
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findOne(super.getEntityId("brotherhood1"));

		paradesSubmittedFinal = this.paradeService.findParadeSubmittedFinalByBrotherhood(brotherhood.getId());
		paradesAcceptedFinal = this.paradeService.findParadeAcceptedFinalByBrotherhood(brotherhood.getId());
		paradesRejectedFinal = this.paradeService.findParadeRejectedFinalByBrotherhood(brotherhood.getId());

		Assert.notNull(paradesRejectedFinal);
		Assert.notNull(paradesSubmittedFinal);
		Assert.notNull(paradesAcceptedFinal);

		super.unauthenticate();
	}

	/*
	 * A: Brotherhood makes a copy of one of their parades.
	 * C: 100% of sentence coverage
	 * D: 100% of data coverage
	 */
	@Test
	public void make_parade_copy_positive_test() {
		super.authenticate("brotherhood1");

		Parade parade, paradeCopied;

		parade = this.paradeService.findOne(super.getEntityId("parade1"));

		paradeCopied = this.paradeService.makeCopy(parade);

		Assert.isTrue(this.paradeService.findAll().contains(paradeCopied));

		super.unauthenticate();
	}

	/*
	 * A: Brotherhood makes a copy of one of their parades.
	 * B: Brotherhood make a copy of a parade that not belongs to him.
	 * C: Approximately 14% of sentence coverage, since it has been covered 4 lines of code of 28 possible.
	 * D: 100% of data coverage
	 */
	@Test(expected = IllegalArgumentException.class)
	public void make_parade_copy_negative_test() {
		super.authenticate("brotherhood1");

		Parade parade, paradeCopied;

		parade = this.paradeService.findOne(super.getEntityId("parade2"));

		paradeCopied = this.paradeService.makeCopy(parade);

		Assert.isTrue(this.paradeService.findAll().contains(paradeCopied));

		super.unauthenticate();
	}

}
