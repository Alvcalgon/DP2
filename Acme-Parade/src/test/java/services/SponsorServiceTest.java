
package services;

import java.util.Arrays;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import repositories.SponsorRepository;
import security.Authority;
import security.UserAccount;
import utilities.AbstractTest;
import domain.Sponsor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class SponsorServiceTest extends AbstractTest {

	// The SUT ----------------------------------------------------------------

	@Autowired
	private SponsorService		sponsorService;

	// Other services ---------------------------------------------------------

	@Autowired
	private SponsorRepository	sponsorRepository;


	// Tests ------------------------------------------------------------------

	/*
	 * REQUIREMENT TESTED: An actor who is not authenticated must be able to:
	 * Register as a sponsor.
	 * 
	 * ANALISYS OF SENTENCE COVERAGE: Approximately 77% of sentence coverage,
	 * since it has been covered 14 lines of code of 18 possible.
	 * 
	 * ANALISYS OF DATA COVERAGE: Approximately 10% of data coverage, because
	 * actors have a lot of attributes with some restrictions.
	 */
	@Test
	public void testRegisterSponsor() {
		Sponsor sponsor, saved;
		UserAccount userAccount;
		Authority auth;

		auth = new Authority();
		auth.setAuthority("SPONSOR");
		userAccount = new UserAccount();

		userAccount.setAuthorities(Arrays.asList(auth));
		userAccount.setUsername("testingUsername");
		userAccount.setPassword("testingPassword");

		sponsor = this.sponsorService.create();
		sponsor.setAddress("C/Test nº99");
		sponsor.setEmail("emailTest@gmail.com");
		sponsor.setName("TestingName");
		sponsor.setMiddleName("TestingMiddleName");
		sponsor.setSurname("TestingSurname");
		sponsor.setPhoto("http://www.photourl12.com");
		sponsor.setPhoneNumber("123 123123123");
		sponsor.setUserAccount(userAccount);

		saved = this.sponsorService.save(sponsor);
		this.sponsorRepository.flush();

		sponsor = this.sponsorRepository.findOne(saved.getId());
		Assert.isTrue(saved.equals(sponsor));
	}

	/*
	 * REQUIREMENT TESTED: An actor who is not authenticated must be able to:
	 * Register as a sponsor.
	 * 
	 * ANALISYS OF SENTENCE COVERAGE: Approximately 77% of sentence coverage,
	 * since it has been covered 14 lines of code of 18 possible.
	 * 
	 * ANALISYS OF DATA COVERAGE: Approximately 10% of data coverage, because
	 * actors have a lot of attributes with some restrictions.
	 */
	@Test(expected = ConstraintViolationException.class)
	public void testRegisterSponsorInvalidPhoto() {
		Sponsor sponsor, saved;
		UserAccount userAccount;
		Authority auth;

		auth = new Authority();
		auth.setAuthority("SPONSOR");
		userAccount = new UserAccount();

		userAccount.setAuthorities(Arrays.asList(auth));
		userAccount.setUsername("testingUsername");
		userAccount.setPassword("testingPassword");

		sponsor = this.sponsorService.create();
		sponsor.setAddress("C/Test nº99");
		sponsor.setEmail("emailTest@gmail.com");
		sponsor.setName("TestingName");
		sponsor.setMiddleName("TestingMiddleName");
		sponsor.setSurname("TestingSurname");
		sponsor.setPhoto("This is an invalid URL");
		sponsor.setPhoneNumber("123 123123123");
		sponsor.setUserAccount(userAccount);

		saved = this.sponsorService.save(sponsor);
		this.sponsorRepository.flush();

		sponsor = this.sponsorRepository.findOne(saved.getId());
		Assert.isTrue(saved.equals(sponsor));
	}

}
