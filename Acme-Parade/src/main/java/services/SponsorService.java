
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.SponsorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Sponsor;

@Service
@Transactional
public class SponsorService {

	// Managed repository -----------------------

	@Autowired
	private SponsorRepository	sponsorRepository;

	// Other supporting services --------------

	@Autowired
	private ActorService		actorService;


	// Constructors ---------------------------

	public SponsorService() {
		super();
	}

	// Simple CRUD methods --------------------

	public Sponsor create() {
		Sponsor result;

		result = new Sponsor();
		result.setUserAccount(this.actorService.createUserAccount(Authority.SPONSOR));

		return result;
	}

	public Sponsor findOne(final int sponsorId) {
		Sponsor result;

		result = this.sponsorRepository.findOne(sponsorId);
		Assert.notNull(result);

		return result;
	}

	public Sponsor findOneToDisplayEdit(final int sponsorId) {
		Assert.isTrue(sponsorId != 0);

		Sponsor result, principal;

		principal = this.findByPrincipal();
		result = this.sponsorRepository.findOne(sponsorId);
		Assert.notNull(result);
		Assert.isTrue(principal.getId() == sponsorId);

		return result;
	}

	public Sponsor save(final Sponsor sponsor) {
		Sponsor result;

		result = (Sponsor) this.actorService.save(sponsor);

		return result;
	}

	// Other business methods -----------------

	protected Sponsor findByPrincipal() {
		Sponsor result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();

		result = this.findByUserAccount(userAccount.getId());
		Assert.notNull(result);

		return result;
	}

	private Sponsor findByUserAccount(final int userAccountId) {
		Sponsor result;

		result = this.sponsorRepository.findByUserAccount(userAccountId);

		return result;
	}

}
