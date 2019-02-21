
package security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserAccountService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private UserAccountRepository	userAccountRepository;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------

	public UserAccountService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	// Other business methods -------------------------------------------------

	public void setLogin(final UserAccount userAccount, final String username, final String password) {
		if (!"".equals(username) && username != null)
			userAccount.setUsername(username);

		if (!"".equals(password) && password != null)
			userAccount.setPassword(password);
	}

	public boolean existUsername(final String username) {
		boolean result;
		UserAccount userAccount;

		userAccount = this.userAccountRepository.findByUsername(username);
		result = !(userAccount == null);

		return result;
	}

}
