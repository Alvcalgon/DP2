
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.BrotherhoodRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Area;
import domain.Brotherhood;
import forms.BrotherhoodForm;
import forms.BrotherhoodRegistrationForm;

@Service
@Transactional
public class BrotherhoodService {

	// Managed repository --------------------------

	@Autowired
	private BrotherhoodRepository	brotherhoodRepository;

	// Other supporting services -------------------

	@Autowired
	private ActorService			actorService;

	@Autowired
	private Validator				validator;

	@Autowired
	private UserAccountService		userAccountService;


	// Constructors -------------------------------

	public BrotherhoodService() {
		super();
	}

	// Simple CRUD methods ------------------------

	public Brotherhood create() {
		Brotherhood result;

		result = new Brotherhood();
		result.setUserAccount(this.actorService.createUserAccount(Authority.BROTHERHOOD));

		return result;
	}

	public Brotherhood findOne(final int brotherhoodId) {
		Brotherhood result;

		result = this.brotherhoodRepository.findOne(brotherhoodId);
		Assert.notNull(result);

		return result;
	}

	public Brotherhood findOneToDisplayEdit(final int actorId) {
		Assert.isTrue(actorId != 0);

		Brotherhood result, principal;

		principal = this.findByPrincipal();
		result = this.brotherhoodRepository.findOne(actorId);
		Assert.notNull(result);
		Assert.isTrue(principal.getId() == actorId);

		return result;
	}

	public Collection<Brotherhood> findAll() {
		Collection<Brotherhood> result;

		result = this.brotherhoodRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Brotherhood save(final Brotherhood brotherhood) {
		Brotherhood result;

		result = (Brotherhood) this.actorService.save(brotherhood);

		return result;
	}

	// Other business methods ---------------------

	public Collection<Brotherhood> findLargest() {
		Collection<Brotherhood> result;

		result = this.brotherhoodRepository.findLargest();

		return result;
	}

	public Collection<Brotherhood> findSmallest() {
		Collection<Brotherhood> result;

		result = this.brotherhoodRepository.findSmallest();

		return result;
	}

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

	public Brotherhood findBrotherhoodByProcession(final int processionId) {
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodRepository.findBrotherhoodByProcession(processionId);

		return brotherhood;

	}

	public Collection<Brotherhood> findByMemberId(final int memberId) {
		Collection<Brotherhood> result;

		result = this.brotherhoodRepository.findByMemberId(memberId);
		Assert.notNull(result);

		return result;
	}

	public Brotherhood reconstruct(final BrotherhoodRegistrationForm registrationForm, final BindingResult binding) {
		Brotherhood result, brotherhoodStored;
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
			result.setTitle(registrationForm.getTitle());
			result.setEstablishmentDate(registrationForm.getEstablishmentDate());
			result.setPictures(registrationForm.getPictures());
			result.setArea(registrationForm.getArea());

			userAccount = result.getUserAccount();
			userAccount.setUsername(registrationForm.getUsername());
			userAccount.setPassword(registrationForm.getPassword());

			this.validateRegistration(result, registrationForm, binding);
		} else {
			result = new Brotherhood();
			brotherhoodStored = this.findOneToDisplayEdit(registrationForm.getId());

			result.setName(registrationForm.getName());
			result.setSurname(registrationForm.getSurname());
			result.setEmail(registrationForm.getEmail());
			result.setPhoneNumber(registrationForm.getPhoneNumber());
			result.setMiddleName(registrationForm.getMiddleName());
			result.setPhoto(registrationForm.getPhoto());
			result.setAddress(registrationForm.getAddress());
			result.setTitle(registrationForm.getTitle());
			result.setEstablishmentDate(registrationForm.getEstablishmentDate());
			result.setPictures(registrationForm.getPictures());
			result.setIsSpammer(brotherhoodStored.getIsSpammer());
			result.setScore(brotherhoodStored.getScore());
			result.setId(brotherhoodStored.getId());
			result.setVersion(brotherhoodStored.getVersion());
			result.setUserAccount(brotherhoodStored.getUserAccount());
			result.setArea(brotherhoodStored.getArea());

		}
		this.validator.validate(result, binding);

		return result;
	}

	private void validateRegistration(final Brotherhood brotherhood, final BrotherhoodRegistrationForm registrationForm, final BindingResult binding) {
		String password, confirmPassword, username;

		password = registrationForm.getPassword();
		confirmPassword = registrationForm.getConfirmPassword();
		username = registrationForm.getUsername();

		if (!password.equals(confirmPassword))
			binding.rejectValue("confirmPassword", "user.missmatch.password", "Does not match with password");
		else if (this.userAccountService.existUsername(username))
			binding.rejectValue("username", "actor.username.used", "Username already in use");
		else if (this.actorService.existEmail(brotherhood.getEmail()))
			binding.rejectValue("email", "actor.email.used", "Email already in use");
		else if (password.length() < 5 || password.length() > 32)
			binding.rejectValue("password", "actor.password.size", "Password must have between 5 and 32 characters");
		else if (username.length() < 5 || username.length() > 32)
			binding.rejectValue("username", "actor.username.size", "Username must have between 5 and 32 characters.");

	}

	public Collection<Brotherhood> findBrotherhoodFromArea(final Area area) {
		Collection<Brotherhood> brotherhoods;

		brotherhoods = this.brotherhoodRepository.findBrotherhoodFromArea(area.getId());

		return brotherhoods;
	}
	public Brotherhood findBrotherhoodToSelectArea() {
		Brotherhood result;

		result = this.findByPrincipal();

		Assert.isNull(result.getArea());

		return result;
	}

	public Brotherhood reconstruct(final BrotherhoodForm brotherhoodForm, final BindingResult binding) {
		final Brotherhood result;

		result = this.findBrotherhoodToSelectArea();
		result.setArea(brotherhoodForm.getArea());
		return result;

	}

	public void checkBrotherhoodHasArea() {
		final Brotherhood principal;

		principal = this.findByPrincipal();

		Assert.isNull(principal.getArea());

	}

	public BrotherhoodRegistrationForm createForm(final Brotherhood brotherhood) {
		BrotherhoodRegistrationForm brotherhoodRegistrationForm;

		brotherhoodRegistrationForm = new BrotherhoodRegistrationForm();

		brotherhoodRegistrationForm.setId(brotherhood.getId());
		brotherhoodRegistrationForm.setName(brotherhood.getName());
		brotherhoodRegistrationForm.setSurname(brotherhood.getSurname());
		brotherhoodRegistrationForm.setAddress(brotherhood.getAddress());
		brotherhoodRegistrationForm.setEmail(brotherhood.getEmail());
		brotherhoodRegistrationForm.setMiddleName(brotherhood.getMiddleName());
		brotherhoodRegistrationForm.setPhoto(brotherhood.getPhoto());
		brotherhoodRegistrationForm.setTitle(brotherhood.getTitle());
		brotherhoodRegistrationForm.setEstablishmentDate(brotherhood.getEstablishmentDate());
		brotherhoodRegistrationForm.setPictures(brotherhood.getPictures());

		return brotherhoodRegistrationForm;

	}

	//Req 22.2.1 (The ratio, the count, the minimum, the maximum, the average, and the standard deviation of the number of brotherhoods per area.)
	public Double[] findDataNumberBrotherhoodPerArea() {
		Double[] result;

		result = this.brotherhoodRepository.findDataNumberBrotherhoodPerArea();
		Assert.notNull(result);

		return result;
	}

}
