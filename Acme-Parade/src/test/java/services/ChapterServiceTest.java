
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


	// Tests ------------------------------------------------------------------

	/*
	 * REQUIREMENT TESTED: An actor who is not authenticated must be able to:
	 * Register to the system as chapter.
	 * 
	 * ANALISYS OF SENTENCE COVERAGE: Approximately 77% of sentence coverage,
	 * since it has been covered 14 lines of code of 18 possible.
	 * 
	 * ANALISYS OF DATA COVERAGE: Approximately 10% of data coverage, because
	 * actors have a lot of attributes with some restrictions.
	 */
	@Test
	public void testRegisterChapter() {
		Chapter chapter, saved;
		Area area;
		UserAccount userAccount;
		Authority auth;
		int areaId;

		areaId = this.getEntityId("area3");
		area = this.areaRepository.findOne(areaId);
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
	}

	/*
	 * REQUIREMENT TESTED: An actor who is not authenticated must be able to:
	 * Register to the system as chapter.
	 * 
	 * BUSINESS RULE UNDER TEST: No area can be co-ordinated by more than one
	 * chapter.
	 * 
	 * ANALISYS OF SENTENCE COVERAGE: Approximately 77% of sentence coverage,
	 * since it has been covered 14 lines of code of 18 possible.
	 * 
	 * ANALISYS OF DATA COVERAGE: Approximately 10% of data coverage, because
	 * actors have a lot of attributes with some restrictions.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testRegisterChapterInvalidArea() {
		Chapter chapter, saved;
		Area area;
		UserAccount userAccount;
		Authority auth;
		int areaId;

		areaId = this.getEntityId("area1");
		area = this.areaRepository.findOne(areaId);
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
	}

	/*
	 * REQUIREMENT TESTED: An actor who is authenticated as an administrator
	 * must be able to display a dashboard with the following information:
	 * The chapters that coordinate at least 10% more parades than average.
	 * 
	 * ANALISYS OF SENTENCE COVERAGE: 100% of sentence coverage.
	 * 
	 * ANALISYS OF DATA COVERAGE: 100% of data coverage.
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

}
