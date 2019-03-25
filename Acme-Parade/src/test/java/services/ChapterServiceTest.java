
package services;

import java.util.Arrays;
import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import repositories.AreaRepository;
import repositories.ChapterRepository;
import security.Authority;
import security.UserAccount;
import utilities.AbstractTest;
import domain.Area;
import domain.Chapter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ChapterServiceTest extends AbstractTest {

	// The SUT ----------------------------------------------------------------

	@Autowired
	private ChapterService		chapterService;

	// Other services ---------------------------------------------------------

	@Autowired
	private AreaRepository		areaRepository;

	@Autowired
	private ChapterRepository	chapterRepository;

	@Autowired
	private AreaService			areaService;


	// Tests ------------------------------------------------------------------

	/*
	 * A: An actor who is not authenticated must be able to:
	 * Register to the system as chapter.
	 * 
	 * B: -
	 * 
	 * C: Approximately 80% of sentence coverage, since it has been
	 * covered 16 lines of code of 20 possible.
	 * 
	 * D: Approximately 7% of data coverage, because actors have a lot
	 * of attributes with several restrictions.
	 */
	@Test
	public void registerChapterDriver() {
		final Object testingData[][] = {
			/*
			 * B: Positive test
			 * 
			 * C: Approximately 80% of sentence coverage, since it has been
			 * covered 16 lines of code of 20 possible.
			 * 
			 * D: Approximately 5% of data coverage, because actors have a lot
			 * of attributes with several restrictions.
			 */
			{
				"area3", null
			},

			/*
			 * B: Positive test
			 * 
			 * C: Approximately 75% of sentence coverage, since it has been
			 * covered 15 lines of code of 20 possible.
			 * 
			 * D: Approximately 5% of data coverage, because actors have a lot
			 * of attributes with several restrictions.
			 */
			{
				null, null
			},

			/*
			 * B: No area can be coordinated by more than one chapter.
			 * 
			 * C: Approximately 20% of sentence coverage, since it has been
			 * covered 4 lines of code of 20 possible.
			 * 
			 * D: Approximately 5% of data coverage, because actors have a lot
			 * of attributes with several restrictions.
			 */
			{
				"area1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.registerChapterTemplate((String) testingData[i][0], ((Class<?>) testingData[i][1]));
	}

	protected void registerChapterTemplate(final String areaName, final Class<?> expected) {
		Class<?> caught;
		Chapter chapter, saved;
		Area area;
		UserAccount userAccount;
		Authority auth;
		int areaId;

		super.startTransaction();

		caught = null;
		area = null;
		try {
			if (areaName != null) {
				areaId = this.getEntityId(areaName);
				area = this.areaRepository.findOne(areaId);
			}

			auth = new Authority();
			auth.setAuthority("CHAPTER");
			userAccount = new UserAccount();

			userAccount.setAuthorities(Arrays.asList(auth));
			userAccount.setUsername("testingUsername");
			userAccount.setPassword("testingPassword");

			chapter = this.chapterService.create();
			chapter.setAddress("C/Test nº99");
			chapter.setArea(area);
			chapter.setEmail("emailTest@gmail.com");
			chapter.setName("TestingName");
			chapter.setMiddleName("TestingMiddleName");
			chapter.setSurname("TestingSurname");
			chapter.setPhoto("http://www.photourl12.com");
			chapter.setPhoneNumber("123 123123123");
			chapter.setTitle("TestingTitle");
			chapter.setUserAccount(userAccount);

			saved = this.chapterService.save(chapter);
			this.chapterRepository.flush();

			chapter = this.chapterRepository.findOne(saved.getId());
			Assert.isTrue(saved.equals(chapter));
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.rollbackTransaction();
		super.checkExceptions(expected, caught);
	}

	/*
	 * A: An actor who is authenticated as an administrator must be able to
	 * display a dashboard with the following information:
	 * The chapters that coordinate at least 10% more parades than average.
	 * 
	 * B: Positive test
	 * 
	 * C: 100% of sentence coverage.
	 * 
	 * D: 100% of data coverage.
	 */
	@Test
	public void testChaptersCoordinateLeast10MoreParadasThanAverage() {
		Collection<Chapter> chapters;
		Chapter test;
		int chapterTestId;

		chapterTestId = this.getEntityId("chapter2");
		test = this.chapterRepository.findOne(chapterTestId);

		chapters = this.chapterService.chaptersCoordinateLeast10MoreParadasThanAverage();

		Assert.isTrue(chapters.contains(test));
	}

	/*
	 * A: Requirement tested: Req2 (B-level), 1: Self-assign an area to co-ordinate. Once an area is self-assigned, it cannot be changed.
	 * 
	 * B: It is assigned to a chapter, which already coordinates an area, an already coordinated area.
	 * 
	 * C: Analysis of sentence coverage: 23%(3 lines /13 lines)
	 * 
	 * D: Analysis of data coverage: Intentionally blank
	 */
	@Test(expected = IllegalArgumentException.class)
	public void selfAssignedArea_negative_test() {
		super.authenticate("chapter1");

		int areaId;
		Area area;

		areaId = super.getEntityId("area1");
		area = this.areaService.findOne(areaId);

		this.chapterService.selfAssignedArea(area);

		super.unauthenticate();
	}

	/*
	 * A: Requirement tested: Req2 (B-level), 1: Self-assign an area to co-ordinate. Once an area is self-assigned, it cannot be changed.
	 * 
	 * B: It is assigned to a chapter, which already coordinates an area, an area without coordinating
	 * 
	 * C: Analysis of sentence coverage: 76.9%(10 lines /13 lines)
	 * 
	 * D: Analysis of data coverage: Intentionally blank
	 */
	@Test(expected = IllegalArgumentException.class)
	public void selfAssignedArea_negative2_test() {
		super.authenticate("chapter1");

		int areaId;
		Area area;

		areaId = super.getEntityId("area3");
		area = this.areaService.findOne(areaId);

		this.chapterService.selfAssignedArea(area);

		super.unauthenticate();
	}

	/*
	 * A: Requirement tested: Req2 (B-level), 1: Self-assign an area to co-ordinate. Once an area is self-assigned, it cannot be changed.
	 * 
	 * B: An already coordinated area is assigned to a chapter.
	 * 
	 * C: Analysis of sentence coverage: 23%(3 lines /13 lines)
	 * 
	 * D: Analysis of data coverage: Intentionally blank
	 */
	@Test(expected = IllegalArgumentException.class)
	public void selfAssignedArea_negative3_test() {
		super.authenticate("chapter3");

		int areaId;
		Area area;

		areaId = super.getEntityId("area1");
		area = this.areaService.findOne(areaId);

		this.chapterService.selfAssignedArea(area);

		super.unauthenticate();
	}

	/*
	 * A: Requirement tested: Req2 (B-level), 1: Self-assign an area to co-ordinate. Once an area is self-assigned, it cannot be changed.
	 * 
	 * C: Analysis of sentence coverage: 54.5%(6 lines /11 lines)
	 * 
	 * D: Analysis of data coverage: Intentionally blank
	 */
	@Test
	public void selfAssignedArea_positive_test() {
		super.authenticate("chapter3");

		int areaId;
		Area area;

		areaId = super.getEntityId("area3");
		area = this.areaService.findOne(areaId);

		this.chapterService.selfAssignedArea(area);

		super.unauthenticate();
	}

	/*
	 * A: Requirement tested: level A: requirement 14.1 (Listing chapters).
	 * C: Analysis of sentence coverage: 100%
	 * D: Analysis of data coverage: intentionally blank.
	 */
	@Test
	public void find_all_positive_test() {
		Collection<Chapter> all;

		all = this.chapterService.findAll();

		Assert.notNull(all);
		Assert.isTrue(!all.isEmpty());
	}

}
