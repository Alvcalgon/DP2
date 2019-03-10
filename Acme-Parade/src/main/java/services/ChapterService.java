
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ChapterRepository;
import security.LoginService;
import security.UserAccount;
import domain.Chapter;

@Service
@Transactional
public class ChapterService {

	// Managed repository ----------------------
	@Autowired
	private ChapterRepository	chapterRepository;


	// Other supporting services --------------

	// Constructors ---------------------------
	public ChapterService() {
		super();
	}

	// Simple CRUD methods --------------------

	// Other business methods -----------------
	protected Chapter findByPrincipal() {
		Chapter result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();

		result = this.findByUserAccount(userAccount.getId());
		Assert.notNull(result);

		return result;
	}

	private Chapter findByUserAccount(final int userAccountId) {
		Chapter result;

		result = this.chapterRepository.findByUserAccount(userAccountId);

		return result;
	}

}
