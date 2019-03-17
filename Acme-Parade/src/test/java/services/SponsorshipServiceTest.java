
package services;

import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.joda.time.IllegalFieldValueException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.CreditCard;
import domain.Sponsorship;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class SponsorshipServiceTest extends AbstractTest {

	// The SUT ---------------------------
	@Autowired
	private SponsorshipService	sponsorshipService;


	// Other supporting services ---------

	// Test ------------------------------

	/*
	 * A: Requirement tested: level A: requirement 16.1 (Display a sponsorship).
	 * B: The business rule that is intended to be broken: a sponsor try to display a other people's sponsorship.
	 * C: Analysis of sentence coverage: Sequence.
	 * D: Analysis of data coverage: intentionally blank.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void display_negative_test() {
		super.authenticate("sponsor1");

		int sponsorshipId;
		Sponsorship sponsorship;

		sponsorshipId = super.getEntityId("sponsorship1");
		sponsorship = this.sponsorshipService.findOneToEditDisplay(sponsorshipId);

		Assert.isNull(sponsorship);

		super.unauthenticate();
	}

	/*
	 * A: Requirement tested: level A: requirement 16.1 (Display a sponsorship).
	 * C: Analysis of sentence coverage: Sequence.
	 * D: Analysis of data coverage: intentionally blank.
	 */
	@Test
	public void display_positive_test() {
		super.authenticate("sponsor2");

		int sponsorshipId;
		Sponsorship sponsorship;

		sponsorshipId = super.getEntityId("sponsorship1");
		sponsorship = this.sponsorshipService.findOneToEditDisplay(sponsorshipId);

		Assert.notNull(sponsorship);

		super.unauthenticate();
	}

	/*
	 * A: Requirement tested: level A: requirement 16.1 (Listing sponsorships).
	 * C: Analysis of sentence coverage: Sequence.
	 * D: Analysis of data coverage: intentionally blank.
	 */
	@Test
	public void find_all_positive_test() {
		super.authenticate("sponsor2");

		Collection<Sponsorship> all;

		all = this.sponsorshipService.findAllByPrincipal();

		Assert.notNull(all);
		Assert.isTrue(!all.isEmpty());

		super.unauthenticate();
	}

	/*
	 * A: Requirement tested: level A: requirement 11:
	 * C: Analysis of sentence coverage: Sequence
	 * D: Analysis of data coverage: intentionally blank
	 */
	@Test
	public void create_test() {
		super.authenticate("sponsor2");

		int paradeId;
		Sponsorship sponsorship;

		paradeId = super.getEntityId("parade4");
		sponsorship = this.sponsorshipService.create(paradeId);

		Assert.notNull(sponsorship);
		Assert.notNull(sponsorship.getSponsor());
		Assert.notNull(sponsorship.getParade());
		Assert.isTrue(sponsorship.getIsActive());
		Assert.isNull(sponsorship.getBanner());
		Assert.isNull(sponsorship.getTargetURL());
		Assert.isNull(sponsorship.getCreditCard());

		super.unauthenticate();
	}

	/*
	 * A: Requirement tested: level A: requirements 13, 16.1 (Edit a sponsorship).
	 * B: The business rule that is intended to be broken: a sponsor try to edit a other people's sponsorship.
	 * C: Analysis of sentence coverage: Sequence.
	 * D: Analysis of data coverage: intentionally blank.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void save_negative_test_uno() {
		super.authenticate("sponsor1");

		int sponsorshipId;
		Sponsorship sponsorship, saved;

		sponsorshipId = super.getEntityId("sponsorship1");
		sponsorship = this.sponsorshipService.findOne(sponsorshipId);

		sponsorship.setTargetURL("https://www.marca.com/");

		saved = this.sponsorshipService.save(sponsorship);

		Assert.isNull(saved);

		super.unauthenticate();
	}

	/*
	 * A: Requirement tested: level A: requirements 13, 16.1 (Edit a sponsorship).
	 * C: Analysis of sentence coverage: Sequence.
	 * D: Analysis of data coverage: intentionally blank.
	 */
	@Test
	public void save_positive_test() {
		super.authenticate("sponsor2");

		int sponsorshipId;
		Sponsorship sponsorship, saved;

		sponsorshipId = super.getEntityId("sponsorship1");

		sponsorship = this.sponsorshipService.findOne(sponsorshipId);
		sponsorship.setTargetURL("https://www.as.com/");

		saved = this.sponsorshipService.save(sponsorship);
		this.sponsorshipService.flush();

		Assert.notNull(saved);
		Assert.isTrue(saved.getId() != 0);

		super.unauthenticate();
	}

	@Test
	public void driverInsert() {
		final Object testingData[][] = {
			/*
			 * A: Requirement tested: level A: requirements 11, 13, 16.1 (Create a sponsorship)
			 * B: The business rule that is intended to be broken: invalid data in Sponsorship::banner.
			 * C: Analysis of sentence coverage: Sequence.
			 * D: Analysis of data coverage: sponsorship::banner is null.
			 */
			{
				"sponsor2", "parade4", null, "https://www.marca.com/", "Pepito Perez", "MCARD", "5431423328867769", "12", "22", 774, ConstraintViolationException.class
			},
			/*
			 * A: Requirement tested: level A: requirements 11, 13, 16.1 (Create a sponsorship)
			 * B: The business rule that is intended to be broken: invalid data in Sponsorship::banner.
			 * C: Analysis of sentence coverage: Sequence.
			 * D: Analysis of data coverage: sponsorship::banner is empty string.
			 */
			{
				"sponsor2", "parade4", "", "https://www.marca.com/", "Pepito Perez", "MCARD", "5431423328867769", "12", "22", 774, ConstraintViolationException.class
			},
			/*
			 * A: Requirement tested: level A: requirements 11, 13, 16.1 (Create a sponsorship)
			 * B: The business rule that is intended to be broken: invalid data in Sponsorship::targetURL.
			 * C: Analysis of sentence coverage: Sequence.
			 * D: Analysis of data coverage: sponsorship::targetURL is null.
			 */
			{
				"sponsor2", "parade4", "https://www.digitalprinting.co.uk/media/images/products/slides/31/vinyl-pvc-banners-1.jpg", null, "Pepito Perez", "MCARD", "5431423328867769", "12", "22", 774, ConstraintViolationException.class
			},
			/*
			 * A: Requirement tested: level A: requirements 11, 13, 16.1 (Create a sponsorship)
			 * B: The business rule that is intended to be broken: invalid data in Sponsorship::targetURL.
			 * C: Analysis of sentence coverage: Sequence.
			 * D: Analysis of data coverage: sponsorship::targetURL is empty string.
			 */
			{
				"sponsor2", "parade4", "https://www.digitalprinting.co.uk/media/images/products/slides/31/vinyl-pvc-banners-1.jpg", "", "Pepito Perez", "MCARD", "5431423328867769", "12", "22", 774, ConstraintViolationException.class
			},
			/*
			 * A: Requirement tested: level A: requirements 11, 13, 16.1 (Create a sponsorship)
			 * B: The business rule that is intended to be broken: invalid data in Sponsorship::creditCard::holder.
			 * C: Analysis of sentence coverage: Sequence.
			 * D: Analysis of data coverage: sponsorship::creditCard::holder is null.
			 */
			{
				"sponsor2", "parade4", "https://www.digitalprinting.co.uk/media/images/products/slides/31/vinyl-pvc-banners-1.jpg", "https://www.marca.com/", null, "MCARD", "5431423328867769", "12", "22", 774, ConstraintViolationException.class
			},
			/*
			 * A: Requirement tested: level A: requirements 11, 13, 16.1 (Create a sponsorship)
			 * B: The business rule that is intended to be broken: invalid data in Sponsorship::creditCard::holder.
			 * C: Analysis of sentence coverage: Sequence.
			 * D: Analysis of data coverage: sponsorship::creditCard::holder is empty string.
			 */
			{
				"sponsor2", "parade4", "https://www.digitalprinting.co.uk/media/images/products/slides/31/vinyl-pvc-banners-1.jpg", "https://www.marca.com/", "", "MCARD", "5431423328867769", "12", "22", 774, ConstraintViolationException.class
			},
			/*
			 * A: Requirement tested: level A: requirements 11, 13, 16.1 (Create a sponsorship)
			 * B: The business rule that is intended to be broken: invalid data in Sponsorship::creditCard::make.
			 * C: Analysis of sentence coverage: Sequence.
			 * D: Analysis of data coverage: sponsorship::creditCard::make is null.
			 */
			{
				"sponsor2", "parade4", "https://www.digitalprinting.co.uk/media/images/products/slides/31/vinyl-pvc-banners-1.jpg", "https://www.marca.com/", "Pepito Perez", null, "5431423328867769", "12", "22", 774, ConstraintViolationException.class
			},
			/*
			 * A: Requirement tested: level A: requirements 11, 13, 16.1 (Create a sponsorship)
			 * B: The business rule that is intended to be broken: invalid data in Sponsorship::creditCard::make.
			 * C: Analysis of sentence coverage: Sequence.
			 * D: Analysis of data coverage: sponsorship::creditCard::make is empty string.
			 */
			{
				"sponsor2", "parade4", "https://www.digitalprinting.co.uk/media/images/products/slides/31/vinyl-pvc-banners-1.jpg", "https://www.marca.com/", "Pepito Perez", "", "5431423328867769", "12", "22", 774, ConstraintViolationException.class
			},
			/*
			 * A: Requirement tested: level A: requirements 11, 13, 16.1 (Create a sponsorship)
			 * B: The business rule that is intended to be broken: invalid data in Sponsorship::creditCard::number.
			 * C: Analysis of sentence coverage: Sequence.
			 * D: Analysis of data coverage: sponsorship::creditCard::number is null .
			 */
			{
				"sponsor2", "parade4", "https://www.digitalprinting.co.uk/media/images/products/slides/31/vinyl-pvc-banners-1.jpg", "https://www.marca.com/", "Pepito Perez", "MCARD", null, "12", "22", 774, ConstraintViolationException.class
			},
			/*
			 * A: Requirement tested: level A: requirements 11, 13, 16.1 (Create a sponsorship)
			 * B: The business rule that is intended to be broken: invalid data in Sponsorship::creditCard::number.
			 * C: Analysis of sentence coverage: Sequence.
			 * D: Analysis of data coverage: sponsorship::creditCard::number is a empty string.
			 */
			{
				"sponsor2", "parade4", "https://www.digitalprinting.co.uk/media/images/products/slides/31/vinyl-pvc-banners-1.jpg", "https://www.marca.com/", "Pepito Perez", "MCARD", "", "12", "22", 774, ConstraintViolationException.class
			},
			/*
			 * A: Requirement tested: level A: requirements 11, 13, 16.1 (Create a sponsorship)
			 * B: The business rule that is intended to be broken: invalid data in Sponsorship::creditCard::number.
			 * C: Analysis of sentence coverage: Sequence.
			 * D: Analysis of data coverage: sponsorship::creditCard::number is a invalid value.
			 */
			{
				"sponsor2", "parade4", "https://www.digitalprinting.co.uk/media/images/products/slides/31/vinyl-pvc-banners-1.jpg", "https://www.marca.com/", "Pepito Perez", "MCARD", "1111", "12", "22", 774, ConstraintViolationException.class
			},
			/*
			 * A: Requirement tested: level A: requirements 11, 13, 16.1 (Create a sponsorship)
			 * B: The business rule that is intended to be broken: invalid data in Sponsorship::creditCard::expirationMonth.
			 * C: Analysis of sentence coverage: Sequence.
			 * D: Analysis of data coverage: sponsorship::creditCard::expirationMonth is null.
			 */
			{
				"sponsor2", "parade4", "https://www.digitalprinting.co.uk/media/images/products/slides/31/vinyl-pvc-banners-1.jpg", "https://www.marca.com/", "Pepito Perez", "MCARD", "5431423328867769", null, "22", 774, IllegalArgumentException.class
			},
			/*
			 * A: Requirement tested: level A: requirements 11, 13, 16.1 (Create a sponsorship)
			 * B: The business rule that is intended to be broken: invalid data in Sponsorship::creditCard::expirationMonth.
			 * C: Analysis of sentence coverage: Sequence.
			 * D: Analysis of data coverage: sponsorship::creditCard::expirationMonth is empty string.
			 */
			{
				"sponsor2", "parade4", "https://www.digitalprinting.co.uk/media/images/products/slides/31/vinyl-pvc-banners-1.jpg", "https://www.marca.com/", "Pepito Perez", "MCARD", "5431423328867769", "", "22", 774, IllegalArgumentException.class
			},
			/*
			 * A: Requirement tested: level A: requirements 11, 13, 16.1 (Create a sponsorship)
			 * B: The business rule that is intended to be broken: invalid data in Sponsorship::creditCard::expirationMonth.
			 * C: Analysis of sentence coverage: Sequence.
			 * D: Analysis of data coverage: sponsorship::creditCard::expirationMonth out of range (1,12): 0.
			 */

			{
				"sponsor2", "parade4", "https://www.digitalprinting.co.uk/media/images/products/slides/31/vinyl-pvc-banners-1.jpg", "https://www.marca.com/", "Pepito Perez", "MCARD", "5431423328867769", "0", "22", 774, IllegalFieldValueException.class
			},
			/*
			 * A: Requirement tested: level A: requirements 11, 13, 16.1 (Create a sponsorship)
			 * B: The business rule that is intended to be broken: invalid data in Sponsorship::creditCard::expirationMonth.
			 * C: Analysis of sentence coverage: Sequence.
			 * D: Analysis of data coverage: sponsorship::creditCard::expirationMonth out of range (1,12): 13.
			 */
			{
				"sponsor2", "parade4", "https://www.digitalprinting.co.uk/media/images/products/slides/31/vinyl-pvc-banners-1.jpg", "https://www.marca.com/", "Pepito Perez", "MCARD", "5431423328867769", "13", "22", 774, IllegalFieldValueException.class
			},

			/*
			 * A: Requirement tested: level A: requirements 11, 13, 16.1 (Create a sponsorship)
			 * B: The business rule that is intended to be broken: invalid data in Sponsorship::creditCard::expirationYear.
			 * C: Analysis of sentence coverage: Sequence.
			 * D: Analysis of data coverage: sponsorship::creditCard::expirationYear is null.
			 */
			{
				"sponsor2", "parade4", "https://www.digitalprinting.co.uk/media/images/products/slides/31/vinyl-pvc-banners-1.jpg", "https://www.marca.com/", "Pepito Perez", "MCARD", "5431423328867769", "12", null, 774, IllegalArgumentException.class
			},
			/*
			 * A: Requirement tested: level A: requirements 11, 13, 16.1 (Create a sponsorship)
			 * B: The business rule that is intended to be broken: invalid data in Sponsorship::creditCard::expirationYear.
			 * C: Analysis of sentence coverage: Sequence.
			 * D: Analysis of data coverage: sponsorship::creditCard::expirationYear is empty string.
			 */
			{
				"sponsor2", "parade4", "https://www.digitalprinting.co.uk/media/images/products/slides/31/vinyl-pvc-banners-1.jpg", "https://www.marca.com/", "Pepito Perez", "MCARD", "5431423328867769", "12", "", 774, IllegalArgumentException.class
			},
			/*
			 * A: Requirement tested: level A: requirements 11, 13, 16.1 (Create a sponsorship)
			 * B: The business rule that is intended to be broken: invalid data in Sponsorship::creditCard::expirationYear.
			 * C: Analysis of sentence coverage: Sequence.
			 * D: Analysis of data coverage: sponsorship::creditCard::expirationYear is a invalid string: 0.0.
			 */
			{
				"sponsor2", "parade4", "https://www.digitalprinting.co.uk/media/images/products/slides/31/vinyl-pvc-banners-1.jpg", "https://www.marca.com/", "Pepito Perez", "MCARD", "5431423328867769", "12", "0.0", 774, IllegalArgumentException.class
			},
			/*
			 * A: Requirement tested: level A: requirements 11, 13, 16.1 (Create a sponsorship)
			 * B: The business rule that is intended to be broken: invalid data in Sponsorship::creditCard::expirationYear.
			 * C: Analysis of sentence coverage: Sequence.
			 * D: Analysis of data coverage: sponsorship::creditCard::expirationYear is a invalid string: -0.0.
			 */
			{
				"sponsor2", "parade4", "https://www.digitalprinting.co.uk/media/images/products/slides/31/vinyl-pvc-banners-1.jpg", "https://www.marca.com/", "Pepito Perez", "MCARD", "5431423328867769", "12", "-0.0", 774, IllegalArgumentException.class
			},
			/*
			 * A: Requirement tested: level A: requirements 11, 13, 16.1 (Create a sponsorship)
			 * B: The business rule that is intended to be broken: invalid data in Sponsorship::creditCard::cvvCode.
			 * C: Analysis of sentence coverage: Sequence.
			 * D: Analysis of data coverage: sponsorship::creditCard::cvvCode out of range (100,999): 99.
			 */
			{
				"sponsor2", "parade4", "https://www.digitalprinting.co.uk/media/images/products/slides/31/vinyl-pvc-banners-1.jpg", "https://www.marca.com/", "Pepito Perez", "MCARD", "5431423328867769", "12", "22", 99, ConstraintViolationException.class
			},
			/*
			 * A: Requirement tested: level A: requirements 11, 13, 16.1 (Create a sponsorship)
			 * B: The business rule that is intended to be broken: invalid data in Sponsorship::creditCard::cvvCode.
			 * C: Analysis of sentence coverage: Sequence.
			 * D: Analysis of data coverage: sponsorship::creditCard::cvvCode out of range (100,999): 1000.
			 */
			{
				"sponsor2", "parade4", "https://www.digitalprinting.co.uk/media/images/products/slides/31/vinyl-pvc-banners-1.jpg", "https://www.marca.com/", "Pepito Perez", "MCARD", "5431423328867769", "12", "22", 1000, ConstraintViolationException.class
			},
			/*
			 * A: Requirement tested: level A: requirements 11, 13, 16.1 (Create a sponsorship)
			 * C: Analysis of sentence coverage: Sequence.
			 * D: Analysis of data coverage: every attribute of sponsorship and sponsorship::creditCard have a valid value.
			 */
			{
				"sponsor2", "parade4", "https://www.digitalprinting.co.uk/media/images/products/slides/31/vinyl-pvc-banners-1.jpg", "https://www.marca.com/", "Pepito Perez", "MCARD", "5431423328867769", "12", "22", 774, null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateInsert((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (String) testingData[i][8], (Integer) testingData[i][9], (Class<?>) testingData[i][10]);

	}

	protected void templateInsert(final String username, final int paradeId, final String banner, final String targetURL, final String holder, final String make, final String number, final String month, final String year, final Integer cvvCode,
		final Class<?> expected) {
		Class<?> caught;
		Sponsorship sponsorship, saved;
		CreditCard creditCard;

		this.startTransaction();

		caught = null;
		try {
			super.authenticate(username);

			creditCard = new CreditCard();
			creditCard.setHolder(holder);
			creditCard.setMake(make);
			creditCard.setNumber(number);
			creditCard.setExpirationMonth(month);
			creditCard.setExpirationYear(year);
			creditCard.setCvvCode(cvvCode);

			sponsorship = this.sponsorshipService.create(paradeId);
			sponsorship.setBanner(banner);
			sponsorship.setTargetURL(targetURL);
			sponsorship.setCreditCard(creditCard);

			saved = this.sponsorshipService.save(sponsorship);
			this.sponsorshipService.flush();

			Assert.notNull(saved);
			Assert.isTrue(saved.getId() != 0);

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	/*
	 * A: Requirement tested: level A: requirements 11, 13, 16.1 (Remove a sponsorship)
	 * B: The business rule that is intended to be broken: a sponsor try to remove a other people's sponsorship
	 * C: Analysis of sentence coverage: Sequence.
	 * D: Analysis of data coverage: intentionally blank.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void remove_negative_test() {
		super.authenticate("sponsor1");

		int sponsorshipId;
		Sponsorship sponsorship;

		sponsorshipId = super.getEntityId("sponsorship1");

		sponsorship = this.sponsorshipService.findOne(sponsorshipId);

		Assert.isTrue(sponsorship.getIsActive());

		this.sponsorshipService.removeBySponsor(sponsorship);
		this.sponsorshipService.flush();

		Assert.isTrue(sponsorship.getIsActive());

		super.unauthenticate();
	}

	/*
	 * A: Requirement tested: level A: requirements 11, 13, 16.1 (Remove a sponsorship)
	 * C: Analysis of sentence coverage: Sequence.
	 * D: Analysis of data coverage: intentionally blank.
	 */
	@Test
	public void remove_positive_test() {
		super.authenticate("sponsor2");

		int sponsorshipId;
		Sponsorship sponsorship;

		sponsorshipId = super.getEntityId("sponsorship1");

		sponsorship = this.sponsorshipService.findOne(sponsorshipId);

		Assert.isTrue(sponsorship.getIsActive());

		this.sponsorshipService.removeBySponsor(sponsorship);
		this.sponsorshipService.flush();

		Assert.isTrue(!sponsorship.getIsActive());

		super.unauthenticate();
	}

	/*
	 * A: Requirement tested: level A: requirement 16.1 (Reactivate a deactivated sponsorship)
	 * C: Analysis of sentence coverage: Sequence.
	 * D: Analysis of data coverage: intentionally blank.
	 */
	@Test
	public void reactivate_test() {
		super.authenticate("sponsor1");

		final int sponsorshipId = super.getEntityId("sponsorship4");
		Sponsorship sponsorship;

		sponsorship = this.sponsorshipService.findOne(sponsorshipId);

		Assert.isTrue(!sponsorship.getIsActive());

		this.sponsorshipService.reactivate(sponsorship);

		Assert.isTrue(sponsorship.getIsActive());

		super.unauthenticate();
	}

	/*
	 * A: Requirement tested: level A: requirement 18.1: Launch a process
	 * that automatically deactivates the sponsorships whose credit cards have expired
	 * C: Analysis of sentence coverage: Loop.
	 * In this case, there are two sponsorships with a expired credit card, so sponsorship::isActive
	 * will be false.
	 * D: Analysis of data coverage: intentionally blank.
	 */
	@Test
	public void deactivateProcess_test() {
		super.authenticate("admin1");

		int sponsorshipId;
		Sponsorship sponsorship_uno, sponsorship_dos;

		sponsorshipId = super.getEntityId("sponsorship10");

		sponsorship_uno = this.sponsorshipService.findOne(sponsorshipId);

		sponsorshipId = super.getEntityId("sponsorship13");

		sponsorship_dos = this.sponsorshipService.findOne(sponsorshipId);

		Assert.isTrue(sponsorship_uno.getIsActive());
		Assert.isTrue(sponsorship_dos.getIsActive());

		this.sponsorshipService.deactivateProcess();

		Assert.isTrue(!sponsorship_uno.getIsActive());
		Assert.isTrue(!sponsorship_dos.getIsActive());

		super.unauthenticate();
	}

	/*
	 * A: Requirement tested: level A: requirement 20: Every a time a parade with sponsorships is displayed, a
	 * random sponsorship must be selected...
	 * C: Analysis of sentence coverage: Conditional: the if condition is true, that is, the number of sponsorship
	 * of parade1 is more than zero.
	 * D: Analysis of data coverage: intentionally blank.
	 */
	@Test
	public void getRandomSponsorship_test_uno() {
		int paradeId;
		Sponsorship sponsorship;

		paradeId = super.getEntityId("parade1");
		sponsorship = this.sponsorshipService.getRandomSponsorship(paradeId);

		Assert.notNull(sponsorship);
	}

	/*
	 * A: Requirement tested: level A: requirement 20: Every a time a parade with sponsorships is displayed, a
	 * random sponsorship must be selected...
	 * C: Analysis of sentence coverage: Conditional: the if condition is false, that is, the number of sponsorship
	 * of parade4 is zero.
	 * D: Analysis of data coverage: intentionally blank.
	 */
	@Test
	public void getRandomSponsorship_test_dos() {
		int paradeId;
		Sponsorship sponsorship;

		paradeId = super.getEntityId("parade4");
		sponsorship = this.sponsorshipService.getRandomSponsorship(paradeId);

		Assert.isNull(sponsorship);
	}

}
