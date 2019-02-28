
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AdministratorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Administrator;
import forms.RegistrationForm;

@Service
@Transactional
public class AdministratorService {

	// Managed repository --------------------------

	@Autowired
	private AdministratorRepository	administratorRepository;

	// Other supporting services -------------------

	@Autowired
	private ActorService			actorService;

	@Autowired
	private Validator				validator;

	@Autowired
	private UserAccountService		userAccountService;


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

	public Administrator findOneToDisplayEdit(final int actorId) {
		Assert.isTrue(actorId != 0);

		Administrator result, principal;

		principal = this.findByPrincipal();
		result = this.administratorRepository.findOne(actorId);
		Assert.notNull(result);
		Assert.isTrue(principal.getId() == actorId);

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

	public Administrator reconstruct(final RegistrationForm registrationForm, final BindingResult binding) {
		Administrator result, administratorStored;
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
			result = new Administrator();
			administratorStored = this.findOneToDisplayEdit(registrationForm.getId());

			result.setName(registrationForm.getName());
			result.setSurname(registrationForm.getSurname());
			result.setEmail(registrationForm.getEmail());
			result.setPhoneNumber(registrationForm.getPhoneNumber());
			result.setMiddleName(registrationForm.getMiddleName());
			result.setPhoto(registrationForm.getPhoto());
			result.setAddress(registrationForm.getAddress());
			result.setIsSpammer(administratorStored.getIsSpammer());
			result.setScore(administratorStored.getScore());
			result.setId(administratorStored.getId());
			result.setVersion(administratorStored.getVersion());
			result.setUserAccount(administratorStored.getUserAccount());

		}
		this.validator.validate(result, binding);

		return result;
	}

	private void validateRegistration(final Administrator administrator, final RegistrationForm registrationForm, final BindingResult binding) {
		String password, confirmPassword, username;

		password = registrationForm.getPassword();
		confirmPassword = registrationForm.getConfirmPassword();
		username = registrationForm.getUsername();

		if (!password.equals(confirmPassword))
			binding.rejectValue("confirmPassword", "user.missmatch.password", "Does not match with password");
		else if (this.userAccountService.existUsername(username))
			binding.rejectValue("username", "actor.username.used", "Username already in use");
		else if (this.actorService.existEmail(administrator.getEmail()))
			binding.rejectValue("email", "actor.email.used", "Email already in use");
		else if (password.length() < 5 || password.length() > 32)
			binding.rejectValue("password", "actor.password.size", "Password must have between 5 and 32 characters");
		else if (username.length() < 5 || username.length() > 32)
			binding.rejectValue("username", "actor.username.size", "Username must have between 5 and 32 characters.");

	}

	public RegistrationForm createForm(final Administrator administrator) {
		RegistrationForm registrationForm;

		registrationForm = new RegistrationForm();

		registrationForm.setName(administrator.getName());
		registrationForm.setMiddleName(administrator.getMiddleName());
		registrationForm.setSurname(administrator.getSurname());
		registrationForm.setEmail(administrator.getEmail());
		registrationForm.setId(administrator.getId());
		registrationForm.setPhoto(administrator.getPhoto());
		registrationForm.setPhoneNumber(administrator.getPhoneNumber());
		registrationForm.setAddress(administrator.getAddress());

		return registrationForm;
	}

}
