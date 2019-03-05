
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

			this.validateEmail(registrationForm.getEmail(), binding);

			if (registrationForm.getUsername().isEmpty() && registrationForm.getPassword().isEmpty() && registrationForm.getConfirmPassword().isEmpty()) // No ha actualizado ningun atributo de user account
				result.setUserAccount(administratorStored.getUserAccount());
			else if (!registrationForm.getUsername().isEmpty() && registrationForm.getPassword().isEmpty() && registrationForm.getConfirmPassword().isEmpty()) {// Modifica el username
				this.validateUsernameEdition(registrationForm.getUsername(), binding);
				if (binding.hasErrors()) {

				} else {
					userAccount = administratorStored.getUserAccount();
					userAccount.setUsername(registrationForm.getUsername());
					result.setUserAccount(userAccount);
				}
			} else if (registrationForm.getUsername().isEmpty() && !registrationForm.getPassword().isEmpty() && !registrationForm.getConfirmPassword().isEmpty()) { // Modifica la password
				this.validatePasswordEdition(registrationForm.getPassword(), registrationForm.getConfirmPassword(), binding);
				if (binding.hasErrors()) {

				} else {
					userAccount = administratorStored.getUserAccount();
					userAccount.setPassword(registrationForm.getPassword());
					result.setUserAccount(userAccount);
				}
			} else if (!registrationForm.getUsername().isEmpty() && !registrationForm.getPassword().isEmpty() && !registrationForm.getConfirmPassword().isEmpty()) { // Modifica el username y la password
				this.validateUsernamePasswordEdition(registrationForm, binding);
				if (binding.hasErrors()) {

				} else {
					userAccount = administratorStored.getUserAccount();
					userAccount.setUsername(registrationForm.getUsername());
					userAccount.setPassword(registrationForm.getPassword());
					result.setUserAccount(userAccount);
				}
			}

		}
		this.validator.validate(result, binding);

		return result;
	}
	private void validateRegistration(final Administrator administrator, final RegistrationForm registrationForm, final BindingResult binding) {
		String password, confirmPassword, username;
		boolean checkBox;

		password = registrationForm.getPassword();
		confirmPassword = registrationForm.getConfirmPassword();
		username = registrationForm.getUsername();
		checkBox = registrationForm.getCheckBoxAccepted();

		this.validateEmail(administrator.getEmail(), binding);
		if (username.trim().equals(""))
			binding.rejectValue("username", "actor.username.blank", "Must entry a username.");
		if (password.trim().equals("") && confirmPassword.trim().equals("")) {
			binding.rejectValue("password", "password.empty", "Must entry a password");
			binding.rejectValue("confirmPassword", "confirmPassword.empty", "Must entry a confirm password");
		}
		if (!password.equals(confirmPassword))
			binding.rejectValue("confirmPassword", "user.missmatch.password", "Does not match with password");
		if (checkBox == false)
			binding.rejectValue("checkBoxAccepted", "actor.checkBox.agree", "Must agree terms and conditions");
		if (this.userAccountService.existUsername(username))
			binding.rejectValue("username", "actor.username.used", "Username already in use");
		if (this.actorService.existEmail(administrator.getEmail()))
			binding.rejectValue("email", "actor.email.used", "Email already in use");
		if (password.length() < 5 || password.length() > 32)
			binding.rejectValue("password", "actor.password.size", "Password must have between 5 and 32 characters");
		if (username.length() < 5 || username.length() > 32)
			binding.rejectValue("username", "actor.username.size", "Username must have between 5 and 32 characters.");

	}

	private void validateUsernamePasswordEdition(final RegistrationForm registrationForm, final BindingResult binding) {
		String password, confirmPassword, username;

		password = registrationForm.getPassword();
		confirmPassword = registrationForm.getConfirmPassword();
		username = registrationForm.getUsername();

		if (password.trim().equals("") && confirmPassword.trim().equals("")) {
			binding.rejectValue("password", "password.empty", "Must entry a password");
			binding.rejectValue("confirmPassword", "confirmPassword.empty", "Must entry a confirm password");
		}
		if (username.trim().equals(""))
			binding.rejectValue("username", "actor.username.blank", "Must entry a username.");
		if (!password.equals(confirmPassword))
			binding.rejectValue("confirmPassword", "user.missmatch.password", "Does not match with password");
		if (this.userAccountService.existUsername(username))
			binding.rejectValue("username", "actor.username.used", "Username already in use");
		if (password.length() < 5 || password.length() > 32)
			binding.rejectValue("password", "actor.password.size", "Password must have between 5 and 32 characters");
		if (username.length() < 5 || username.length() > 32)
			binding.rejectValue("username", "actor.username.size", "Username must have between 5 and 32 characters.");

	}

	private void validateUsernameEdition(final String username, final BindingResult binding) {

		if (username.trim().equals(""))
			binding.rejectValue("username", "actor.username.blank", "Must entry a username.");
		if (this.userAccountService.existUsername(username))
			binding.rejectValue("username", "actor.username.used", "Username already in use");
		if (username.length() < 5 || username.length() > 32)
			binding.rejectValue("username", "actor.username.size", "Username must have between 5 and 32 characters.");

	}

	private void validatePasswordEdition(final String password, final String confirmPassword, final BindingResult binding) {

		if (password.trim().equals("") && confirmPassword.trim().equals("")) {
			binding.rejectValue("password", "password.empty", "Must entry a password");
			binding.rejectValue("confirmPassword", "confirmPassword.empty", "Must entry a confirm password");
		}
		if (!password.equals(confirmPassword))
			binding.rejectValue("confirmPassword", "user.missmatch.password", "Does not match with password");
		if (password.length() < 5 || password.length() > 32)
			binding.rejectValue("password", "actor.password.size", "Password must have between 5 and 32 characters");

	}

	private void validateEmail(final String email, final BindingResult binding) {

		if (!email.matches("[A-Za-z0-9]+@[a-zA-Z0-9.-]+|[\\w\\s]+[\\<][A-Za-z0-9]+@[a-zA-Z0-9.-]+[\\>]|[A-Za-z0-9]+@|[\\w\\s]+[\\<][A-Za-z0-9]+@+[\\>]"))
			binding.rejectValue("email", "actor.email.error", "Invalid email pattern");

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
		registrationForm.setCheckBoxAccepted(false);

		return registrationForm;
	}

}
