
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.BrotherhoodRepository;
import security.LoginService;
import security.UserAccount;
import domain.Brotherhood;

@Service
@Transactional
public class BrotherhoodService {

	// Managed repository --------------------------

	@Autowired
	private BrotherhoodRepository	brotherhoodRepository;


	// Other supporting services -------------------

	// Constructors -------------------------------

	public BrotherhoodService() {
		super();
	}

	// Simple CRUD methods ------------------------

	// Other business methods ---------------------

	public Brotherhood findByPrincipal() {
		Brotherhood result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();

		result = this.findByUserAccount(userAccount.getId());
		Assert.notNull(result);

		return result;
	}

	private Brotherhood findByUserAccount(final int userAccountId) {
		Brotherhood result;

		result = this.brotherhoodRepository.findByUserAccount(userAccountId);

		return result;
	}

}
