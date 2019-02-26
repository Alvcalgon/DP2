
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AdministratorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Administrator;

@Service
@Transactional
public class AdministratorService {

	// Managed repository --------------------------

	@Autowired
	private AdministratorRepository	administratorRepository;

	// Other supporting services -------------------

	@Autowired
	private ActorService			actorService;


	// Constructors -------------------------------

	public AdministratorService() {
		super();
	}

	// Simple CRUD methods ------------------------

	public Administrator create() {
		Administrator result;

		result = new Administrator();
		result.setUserAccount(this.actorService.createUserAccount(Authority.ADMIN));

		return result;

	}

	public Administrator findOne(final int adminId) {
		Administrator result;

		result = this.administratorRepository.findOne(adminId);
		Assert.notNull(result);

		return result;
	}

	public Administrator save(final Administrator administrator) {
		Administrator result;

		result = (Administrator) this.actorService.save(administrator);

		return result;
	}

	// Other business methods ---------------------
	public Administrator findSystem() {
		Administrator result;

		result = this.administratorRepository.findSystem();

		return result;
	}

	public Administrator findByPrincipal() {
		Administrator result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();

		result = this.findByUserAccount(userAccount.getId());
		Assert.notNull(result);

		return result;
	}

	private Administrator findByUserAccount(final int userAccountId) {
		Administrator result;

		result = this.administratorRepository.findByUserAccount(userAccountId);

		return result;
	}

}
