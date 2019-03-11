
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SponsorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Sponsor;
import forms.RegistrationForm;

@Service
@Transactional
public class SponsorService {

	// Managed repository -----------------------

	@Autowired
	private SponsorRepository	sponsorRepository;

	// Other supporting services --------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private Validator			validator;

	@Autowired
	private UserAccountService	userAccountService;


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

	public Sponsor reconstruct(final RegistrationForm registrationForm, final BindingResult binding) {
		Sponsor result, sponsorStored;
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
			result = new Sponsor();
			sponsorStored = this.findOneToDisplayEdit(registrationForm.getId());

			result.setName(registrationForm.getName());
			result.setSurname(registrationForm.getSurname());
			result.setEmail(registrationForm.getEmail());
			result.setPhoneNumber(registrationForm.getPhoneNumber());
			result.setMiddleName(registrationForm.getMiddleName());
			result.setPhoto(registrationForm.getPhoto());
			result.setAddress(registrationForm.getAddress());
			result.setIsSpammer(sponsorStored.getIsSpammer());
			result.setScore(sponsorStored.getScore());
			result.setId(sponsorStored.getId());
			result.setVersion(sponsorStored.getVersion());

			this.validateEmail(registrationForm.getEmail(), binding);

			if (registrationForm.getUsername().isEmpty() && registrationForm.getPassword().isEmpty() && registrationForm.getConfirmPassword().isEmpty()) // No ha actualizado ningun atributo de user account
				result.setUserAccount(sponsorStored.getUserAccount());
			else if (!registrationForm.getUsername().isEmpty() && registrationForm.getPassword().isEmpty() && registrationForm.getConfirmPassword().isEmpty()) {// Modifica el username
				this.validateUsernameEdition(registrationForm.getUsername(), binding);
				if (binding.hasErrors()) {

				} else {
					userAccount = sponsorStored.getUserAccount();
					userAccount.setUsername(registrationForm.getUsername());
					result.setUserAccount(userAccount);
				}
			} else if (registrationForm.getUsername().isEmpty() && !registrationForm.getPassword().isEmpty() && !registrationForm.getConfirmPassword().isEmpty()) { // Modifica la password
				this.validatePasswordEdition(registrationForm.getPassword(), registrationForm.getConfirmPassword(), binding);
				if (binding.hasErrors()) {

				} else {
					userAccount = sponsorStored.getUserAccount();
					userAccount.setPassword(registrationForm.getPassword());
					result.setUserAccount(userAccount);
				}
			} else if (!registrationForm.getUsername().isEmpty() && !registrationForm.getPassword().isEmpty() && !registrationForm.getConfirmPassword().isEmpty()) { // Modifica el username y la password
				this.validateUsernamePasswordEdition(registrationForm, binding);
				if (binding.hasErrors()) {

				} else {
					userAccount = sponsorStored.getUserAccount();
					userAccount.setUsername(registrationForm.getUsername());
					userAccount.setPassword(registrationForm.getPassword());
					result.setUserAccount(userAccount);
				}
			}

		}
		this.validator.validate(result, binding);

		return result;
	}

	private void validateRegistration(final Sponsor sponsor, final RegistrationForm registrationForm, final BindingResult binding) {
		String password, confirmPassword, username;
		boolean checkBox;

		password = registrationForm.getPassword();
		confirmPassword = registrationForm.getConfirmPassword();
		username = registrationForm.getUsername();
		checkBox = registrationForm.getCheckBoxAccepted();

		this.validateEmail(sponsor.getEmail(), binding);
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

		if (!email.matches("[A-Za-z0-9]+@[a-zA-Z0-9.-]+|[\\w\\s]+[\\<][A-Za-z0-9]+@[a-zA-Z0-9.-]+[\\>]"))
			binding.rejectValue("email", "actor.email.error", "Invalid email pattern");

	}

	public RegistrationForm createForm(final Sponsor sponsor) {
		RegistrationForm registrationForm;

		registrationForm = new RegistrationForm();

		registrationForm.setName(sponsor.getName());
		registrationForm.setMiddleName(sponsor.getMiddleName());
		registrationForm.setSurname(sponsor.getSurname());
		registrationForm.setEmail(sponsor.getEmail());
		registrationForm.setId(sponsor.getId());
		registrationForm.setPhoto(sponsor.getPhoto());
		registrationForm.setPhoneNumber(sponsor.getPhoneNumber());
		registrationForm.setAddress(sponsor.getAddress());
		registrationForm.setCheckBoxAccepted(false);

		return registrationForm;
	}

	//	public Collection<Sponsor> topFiveSponsors() {
	//		Collection<Sponsor> results;
	//		Page<Sponsor> sponsors;
	//		Pageable page;
	//
	//		page = new PageRequest(0, 5);
	//
	//		sponsors = this.sponsorRepository.topFiveSponsors(page);
	//
	//		results = sponsors.getContent();
	//
	//		return results;
	//	}

}
