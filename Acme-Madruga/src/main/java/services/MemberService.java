
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.MemberRepository;
import security.Authority;
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

	@Autowired
	private ActorService		actorService;

	@Autowired
	private FinderService		finderService;


	// Constructors -------------------------------

	public MemberService() {
		super();
	}

	// Simple CRUD methods ------------------------

	public Member create() {
		Member result;

		result = new Member();
		result.setUserAccount(this.actorService.createUserAccount(Authority.MEMBER));

		return result;
	}

	public Member findOne(final int memberId) {
		Member result;

		result = this.memberRepository.findOne(memberId);
		Assert.notNull(result);

		return result;
	}

	public Member save(final Member member) {
		Member result;

		result = (Member) this.actorService.save(member);

		if (!this.memberRepository.exists(member.getId()))
			this.finderService.assignNewFinder(result);

		return result;
	}

	// Other business methods ---------------------

	public Double[] findDataNumberMembersPerBrotherhood() {
		Double[] result;

		result = this.memberRepository.findDataNumberMembersPerBrotherhood();
		Assert.notNull(result);

		return result;
	}

	public Member findByPrincipal() {
		Member result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();

		result = this.findByUserAccount(userAccount.getId());
		Assert.notNull(result);

		return result;
	}

	public Collection<Member> findEnroledMemberByBrotherhood(final int brotherhoodId) {

		Collection<Member> results;

		results = this.memberRepository.findEnroledMemberByBrotherhood(brotherhoodId);

		return results;
	}

	private Member findByUserAccount(final int userAccountId) {
		Member result;

		result = this.memberRepository.findByUserAccount(userAccountId);

		return result;
	}

}
