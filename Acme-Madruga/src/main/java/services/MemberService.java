
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.MemberRepository;
import security.LoginService;
import security.UserAccount;
import domain.Member;

@Service
@Transactional
public class MemberService {

	// Managed repository --------------------------

	@Autowired
	private MemberRepository	memberRepository;


	// Other supporting services -------------------

	// Constructors -------------------------------

	public MemberService() {
		super();
	}

	// Simple CRUD methods ------------------------

	// Other business methods ---------------------

	public Member findByPrincipal() {
		Member result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();

		result = this.findByUserAccount(userAccount.getId());
		Assert.notNull(result);

		return result;
	}

	private Member findByUserAccount(final int userAccountId) {
		Member result;

		result = this.memberRepository.findByUserAccount(userAccountId);

		return result;
	}

}
