
package services;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Finder;
import domain.Parade;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class FinderServiceTest extends AbstractTest {

	// The SUT -------------------------
	@Autowired
	private FinderService	finderService;

	@Autowired
	private ParadeService	paradeService;


	// Other supporting services -------

	// Tests ---------------------------
	/*
	 * A: Requirement tested: level B: requirement 19 and 21.2: A member must be able to manage his or her finder
	 * B: The business rule that is intended to be broken: a member try to edit a finder which owner is other member.
	 * C: Analysis of sentence coverage: Conditional. The condition of finderService::validDates is false.
	 * D: Analysis of data coverage: finder::maximumDate and finder::minimumDate area null values.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void save_negative_test_uno() {
		super.authenticate("member2");

		final int finderId = super.getEntityId("finder1");
		Finder finder;
		Collection<Parade> returnedParades;

		finder = this.finderService.findOne(finderId);
		finder.setKeyword("Edited by other member");
		finder.setArea("Edited by other member");
		finder.setMinimumDate(null);
		finder.setMaximumDate(null);

		this.finderService.save(finder);
		returnedParades = finder.getParades();

		Assert.isNull(returnedParades);

		super.unauthenticate();
	}

	/*
	 * A: Requirement tested: level B: requirement 19 and 21.2: A member must be able to manage his or her finder
	 * B: The business rule that is intended to be broken: the condition finder::minimumDate<finder::maximumDate is false
	 * when both attributes has a specific value.
	 * C: Analysis of sentence coverage: Conditional. The condition of finderService::validDates is true.
	 * D: Analysis of data coverage: finder::minimumDate and finder::maximumDate have the same value, finder::keyword and finder::area are
	 * empty string.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void save_negative_test_dos() {
		super.authenticate("member1");

		Finder finder;
		Calendar minimum_date, maximum_date;
		Date min_date, max_date;
		Collection<Parade> returnedParades;

		minimum_date = Calendar.getInstance();
		minimum_date.set(2019, 03, 20);
		min_date = minimum_date.getTime();

		maximum_date = Calendar.getInstance();
		maximum_date.set(2019, 03, 20);
		max_date = maximum_date.getTime();

		finder = this.finderService.findByMemberPrincipal();
		finder.setKeyword("");
		finder.setArea("");
		finder.setMinimumDate(min_date);
		finder.setMaximumDate(max_date);

		this.finderService.save(finder);
		returnedParades = finder.getParades();

		Assert.isNull(returnedParades);

		super.unauthenticate();
	}

	/*
	 * A: Requirement tested: level B: requirement 19 and 21.2: A member must be able to manage his or her finder
	 * B: The business rule that is intended to be broken: the condition finder::minimumDate<finder::maximumDate is false
	 * when both attributes has a specific value.
	 * C: Analysis of sentence coverage: Conditional. The condition of finderService::validDates is true.
	 * D: Analysis of data coverage: finder::minimumDate > finder::maximumDate, finder::keyword and finder::area are
	 * empty string.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void save_negative_test_tres() {
		super.authenticate("member1");

		Finder finder;
		Calendar minimum_date, maximum_date;
		Date min_date, max_date;
		Collection<Parade> returnedParades;

		minimum_date = Calendar.getInstance();
		minimum_date.set(2019, 03, 21);
		min_date = minimum_date.getTime();

		maximum_date = Calendar.getInstance();
		maximum_date.set(2019, 03, 20);
		max_date = maximum_date.getTime();

		finder = this.finderService.findByMemberPrincipal();
		finder.setKeyword("");
		finder.setArea("");
		finder.setMinimumDate(min_date);
		finder.setMaximumDate(max_date);

		this.finderService.save(finder);
		returnedParades = finder.getParades();

		Assert.isNull(returnedParades);

		super.unauthenticate();
	}

	/*
	 * A: Requirement tested: level B: requirement 19 and 21.2: A member must be able to manage his or her finder.
	 * B: The business rule that is intended to be broken: finder::keyword is null
	 * C: Analysis of sentence coverage: Conditional. The condition of finderService::validDates is false.
	 * D: Analysis of data coverage: finder::minimumDate and finder::maximumDate are null values, finder::keyword is null and
	 * finder::area is a empty string.
	 */
	@Test(expected = ConstraintViolationException.class)
	public void save_negative_test_cuatro() {
		super.authenticate("member1");

		Finder finder;
		Collection<Parade> returnedParades;

		finder = this.finderService.findByMemberPrincipal();
		finder.setKeyword(null);
		finder.setArea("area1");
		finder.setMinimumDate(null);
		finder.setMaximumDate(null);

		this.finderService.save(finder);
		this.finderService.flush();

		returnedParades = finder.getParades();

		Assert.notNull(returnedParades);
		Assert.isTrue(!returnedParades.isEmpty());

		super.unauthenticate();
	}

	/*
	 * A: Requirement tested: level B: requirement 19 and 21.2: A member must be able to manage his or her finder.
	 * B: The business rule that is intended to be broken: finder::keyword is null
	 * C: Analysis of sentence coverage: Conditional. The condition of finderService::validDates is false.
	 * D: Analysis of data coverage: finder::minimumDate and finder::maximumDate are null values, finder::keyword is a specific value and
	 * finder::area is null.
	 */
	@Test(expected = ConstraintViolationException.class)
	public void save_negative_test_cinco() {
		super.authenticate("member1");

		Finder finder;
		Collection<Parade> returnedParades;

		finder = this.finderService.findByMemberPrincipal();
		finder.setKeyword("primavera");
		finder.setArea(null);
		finder.setMinimumDate(null);
		finder.setMaximumDate(null);

		this.finderService.save(finder);
		this.finderService.flush();

		returnedParades = finder.getParades();

		Assert.notNull(returnedParades);
		Assert.isTrue(!returnedParades.isEmpty());

		super.unauthenticate();
	}

	/*
	 * A: Requirement tested: level B: requirement 19 and 21.2: A member must be able to manage his or her finder. If a search criterion
	 * is not specified, then every parade meets it.
	 * C: Analysis of sentence coverage: Conditional. The condition of finderService::validDates is false.
	 * D: Analysis of data coverage: finder::minimumDate and finder::maximumDate are null values, finder::keyword and finder::area are
	 * empty strings.
	 */
	@Test
	public void save_positive_test_uno() {
		super.authenticate("member1");

		Finder finder;
		Collection<Parade> returnedParades, all;

		finder = this.finderService.findByMemberPrincipal();
		finder.setKeyword("");
		finder.setArea("");
		finder.setMinimumDate(null);
		finder.setMaximumDate(null);

		this.finderService.save(finder);
		this.finderService.flush();

		returnedParades = finder.getParades();
		all = this.paradeService.findPublishedParade();

		Assert.notNull(returnedParades);
		Assert.isTrue(!returnedParades.isEmpty());
		Assert.isTrue(returnedParades.size() == all.size());

		super.unauthenticate();
	}

	/*
	 * A: Requirement tested: level B: requirement 19 and 21.2: A member must be able to manage his or her finder.
	 * C: Analysis of sentence coverage: Conditional. The condition of finderService::validDates is true.
	 * D: Analysis of data coverage: finder::minimumDate < finder::maximumDate, finder::keyword and finder::area are
	 * empty strings.
	 */
	@Test
	public void save_positive_test_dos() {
		super.authenticate("member1");

		Finder finder;
		Calendar minimum_date, maximum_date;
		Date min_date, max_date;
		Collection<Parade> returnedParades;

		minimum_date = Calendar.getInstance();
		minimum_date.set(2019, 03, 19);
		min_date = minimum_date.getTime();

		maximum_date = Calendar.getInstance();
		maximum_date.set(2019, 03, 20);
		max_date = maximum_date.getTime();

		finder = this.finderService.findByMemberPrincipal();
		finder.setKeyword("");
		finder.setArea("");
		finder.setMinimumDate(min_date);
		finder.setMaximumDate(max_date);

		this.finderService.save(finder);
		this.finderService.flush();

		returnedParades = finder.getParades();

		Assert.notNull(returnedParades);
		Assert.isTrue(returnedParades.isEmpty());

		super.unauthenticate();
	}

	/*
	 * A: Requirement tested: level B: requirement 19 and 21.2: A member must be able to manage his or her finder.
	 * C: Analysis of sentence coverage: Conditional. The condition of finderService::validDates is false.
	 * D: Analysis of data coverage: finder::minimumDate and finder::maximumDate are null values, finder::keyword has a specific value and
	 * finder::area is a empty value.
	 */
	@Test
	public void save_positive_test_tres() {
		super.authenticate("member1");

		Finder finder;
		Collection<Parade> returnedParades;

		finder = this.finderService.findByMemberPrincipal();
		finder.setKeyword("primavera");
		finder.setArea("");
		finder.setMinimumDate(null);
		finder.setMaximumDate(null);

		this.finderService.save(finder);
		this.finderService.flush();

		returnedParades = finder.getParades();

		Assert.notNull(returnedParades);
		Assert.isTrue(!returnedParades.isEmpty());

		super.unauthenticate();
	}

	/*
	 * A: Requirement tested: level B: requirement 19 and 21.2: A member must be able to manage his or her finder.
	 * C: Analysis of sentence coverage: Conditional. The condition of finderService::validDates is false.
	 * D: Analysis of data coverage: finder::minimumDate and finder::maximumDate are null values, finder::keyword has a empty string and
	 * finder::area is a specific value.
	 */
	@Test
	public void save_positive_test_cuatro() {
		super.authenticate("member1");

		Finder finder;
		Collection<Parade> returnedParades;

		finder = this.finderService.findByMemberPrincipal();
		finder.setKeyword("");
		finder.setArea("area1");
		finder.setMinimumDate(null);
		finder.setMaximumDate(null);

		this.finderService.save(finder);
		this.finderService.flush();

		returnedParades = finder.getParades();

		Assert.notNull(returnedParades);
		Assert.isTrue(!returnedParades.isEmpty());

		super.unauthenticate();
	}

	/*
	 * A: Requirement tested: level B: requirement 21.2: A member must be able to manage his or her finder (clear finder)
	 * B: The business rule that is intended to be broken: A member try yo clear a finder which owner is other member.
	 * C: Analysis of sentence coverage: Sequence
	 * D: Analysis of data coverage: intentionally blank
	 */
	@Test(expected = IllegalArgumentException.class)
	public void clear_negative_test() {
		super.authenticate("member2");

		final int finderId = super.getEntityId("finder1");
		Finder finder;
		final Date def_value = new Date(Integer.MIN_VALUE);

		finder = this.finderService.findOne(finderId);
		this.finderService.clear(finder);

		Assert.notNull(finder.getArea());
		Assert.notNull(finder.getMaximumDate());
		Assert.notNull(finder.getMinimumDate());
		Assert.isTrue(!finder.getUpdatedMoment().equals(def_value));

		super.unauthenticate();
	}

	/*
	 * A: Requirement tested: level B: requirement 21.2: A member must be able to manage his or her finder (clear finder).
	 * C: Analysis of sentence coverage: Sequence.
	 * D: Analysis of data coverage: intentionally blank
	 */
	@Test
	public void clear_positive_test() {
		super.authenticate("member1");

		final Finder finder = this.finderService.findByMemberPrincipal();
		this.finderService.clear(finder);
		final Date def_value = new Date(Integer.MIN_VALUE);

		Assert.isTrue(finder.getKeyword().equals(""));
		Assert.isTrue(finder.getArea().equals(""));
		Assert.isNull(finder.getMaximumDate());
		Assert.isNull(finder.getMinimumDate());
		Assert.isTrue(finder.getUpdatedMoment().equals(def_value));

		super.unauthenticate();
	}

	/*
	 * A: Requirement tested: level B: requirement 22.2: The ratio of empty versus non-empty finders.
	 * C: Analysis of sentence coverage: Sequence.
	 * D: Analysis of data coverage: intentionally blank
	 */
	@Test
	public void ratioFinder() {
		Double ratio;

		ratio = this.finderService.findRatioEmptyVsNonEmpty();

		Assert.notNull(ratio);
		Assert.isTrue(ratio >= 0.0);
	}

}
