
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MemberRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Member;
import forms.RegistrationForm;

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
	private Validator			validator;

	@Autowired
	private UserAccountService	userAccountService;

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

	public Member findOneToDisplayEdit(final int actorId) {
		Assert.isTrue(actorId != 0);

		Member result, principal;

		principal = this.findByPrincipal();
		result = this.memberRepository.findOne(actorId);
		Assert.notNull(result);
		Assert.isTrue(principal.getId() == actorId);

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

	public Member findByEnrolmentId(final int enrolmentId) {
		Member result;

		result = this.memberRepository.findByEnrolmentId(enrolmentId);
		Assert.notNull(result);

		return result;
	}

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

	public Member reconstruct(final RegistrationForm registrationForm, final BindingResult binding) {
		Member result;
		UserAccount userAccount;

		if (registrationForm.getId() == 0) {

			result = this.create();
			result.setName(registrationForm.getName());
			result.setSurname(registrationForm.getSurname());
			result.setEmail(registrationForm.getEmail());
			result.setPhoneNumber(registrationForm.getPhoneNumber());
			result.setMiddleName(registrationForm.getMiddleName());
			result.setPhoto(registrationForm.getPhoto());
			result.setAddress(registrationForm.getAddress());
			result.setIsSpammer(registrationForm.getIsSpammer());
			result.setScore(registrationForm.getScore());

			userAccount = result.getUserAccount();
			userAccount.setUsername(registrationForm.getUsername());
			userAccount.setPassword(registrationForm.getPassword());

			this.validateRegistration(result, registrationForm, binding);
		} else {
			result = this.findOneToDisplayEdit(registrationForm.getId());
			result.setName(registrationForm.getName());
			result.setSurname(registrationForm.getSurname());
			result.setEmail(registrationForm.getEmail());
			result.setPhoneNumber(registrationForm.getPhoneNumber());
			result.setMiddleName(registrationForm.getMiddleName());
			result.setPhoto(registrationForm.getPhoto());
			result.setAddress(registrationForm.getAddress());
			result.setIsSpammer(registrationForm.getIsSpammer());
			result.setScore(registrationForm.getScore());

		}
		this.validator.validate(result, binding);

		return result;
	}

	private void validateRegistration(final Member member, final RegistrationForm registrationForm, final BindingResult binding) {
		String password, confirmPassword, username;

		password = registrationForm.getPassword();
		confirmPassword = registrationForm.getConfirmPassword();
		username = registrationForm.getUsername();

		if (!password.equals(confirmPassword))
			binding.rejectValue("confirmPassword", "user.missmatch.password", "Does not match with password");
		else if (this.userAccountService.existUsername(username))
			binding.rejectValue("username", "actor.username.used", "Username already in use");
		else if (this.actorService.existEmail(member.getEmail()))
			binding.rejectValue("email", "actor.email.used", "Email already in use");
		else if (password.length() < 5 || password.length() > 32)
			binding.rejectValue("password", "actor.password.size", "Password must have between 5 and 32 characters");
		else if (username.length() < 5 || username.length() > 32)
			binding.rejectValue("username", "actor.username.size", "Username must have between 5 and 32 characters.");

	}

	public String validateName(final RegistrationForm registrationForm, final BindingResult binding) {
		String result;

		result = registrationForm.getName();
		if (result.equals("") || result.equals(null))
			binding.rejectValue("name", "name.error.blank", "Must not be blank");

		return result;

	}

	public String validateSurname(final RegistrationForm registrationForm, final BindingResult binding) {
		String result;

		result = registrationForm.getSurname();
		if (result.equals("") || result.equals(null))
			binding.rejectValue("surname", "surname.error.blank", "Must not be blank");

		return result;

	}

	public String validateEmail(final RegistrationForm registrationForm, final BindingResult binding) {
		String result;

		result = registrationForm.getEmail();
		if (result.equals("") || result.equals(null))
			binding.rejectValue("email", "email.error.blank", "Must not be blank");

		return result;

	}

}
