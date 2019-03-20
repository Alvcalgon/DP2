
package services;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ChapterRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Area;
import domain.Chapter;
import forms.ChapterForm;
import forms.ChapterRegistrationForm;

@Service
@Transactional
public class ChapterService {

	// Managed repository ----------------------
	@Autowired
	private ChapterRepository	chapterRepository;

	// Other supporting services --------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private Validator			validator;

	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private AreaService			areaService;

	@Autowired
	private ParadeService		paradeService;


	// Constructors ---------------------------
	public ChapterService() {
		super();
	}

	// Simple CRUD methods --------------------

	public Chapter create() {
		Chapter result;

		result = new Chapter();
		result.setUserAccount(this.actorService.createUserAccount(Authority.CHAPTER));

		return result;
	}

	public Chapter findOne(final int chapterId) {
		Chapter result;

		result = this.chapterRepository.findOne(chapterId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Chapter> findAll() {
		Collection<Chapter> results;

		results = this.chapterRepository.findAll();

		return results;
	}

	public Chapter findOneToDisplayEdit(final int chapterId) {
		Assert.isTrue(chapterId != 0);

		Chapter result, principal;

		principal = this.findByPrincipal();
		result = this.chapterRepository.findOne(chapterId);
		Assert.notNull(result);
		Assert.isTrue(principal.getId() == chapterId);

		return result;
	}

	public Chapter save(final Chapter chapter) {
		Chapter result;

		if (chapter.getArea() != null && chapter.getId() == 0)
			Assert.isTrue(this.areaService.findAreasNotAssigned().contains(chapter.getArea()));

		result = (Chapter) this.actorService.save(chapter);

		return result;
	}

	// Other business methods -----------------
	public void selfAssignedArea(final Area area) {
		Assert.notNull(area);
		Assert.isTrue(area.getId() != 0 && this.areaService.findAreasNotAssigned().contains(area));

		Chapter principal;

		principal = this.findByPrincipal();

		// Once an area is self-assigned, it cannot be changed.
		Assert.isNull(principal.getArea());

		principal.setArea(area);
	}

	public Chapter findByPrincipal() {
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

	private Chapter findChapterToSelectArea() {
		Chapter result;

		result = this.findByPrincipal();

		Assert.isNull(result.getArea());

		return result;
	}

	public Chapter reconstruct(final ChapterForm chapterForm, final BindingResult binding) {
		final Chapter result;

		result = this.findChapterToSelectArea();
		result.setArea(chapterForm.getArea());
		return result;

	}

	public Chapter reconstruct(final ChapterRegistrationForm registrationForm, final BindingResult binding) {
		Chapter result, chapterStored;
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
			result.setArea(registrationForm.getArea());

			userAccount = result.getUserAccount();
			userAccount.setUsername(registrationForm.getUsername());
			userAccount.setPassword(registrationForm.getPassword());

			this.validateRegistration(result, registrationForm, binding);
		} else {
			result = new Chapter();
			chapterStored = this.findOneToDisplayEdit(registrationForm.getId());

			result.setName(registrationForm.getName());
			result.setSurname(registrationForm.getSurname());
			result.setEmail(registrationForm.getEmail());
			result.setPhoneNumber(registrationForm.getPhoneNumber());
			result.setMiddleName(registrationForm.getMiddleName());
			result.setPhoto(registrationForm.getPhoto());
			result.setAddress(registrationForm.getAddress());
			result.setIsSpammer(chapterStored.getIsSpammer());
			result.setScore(chapterStored.getScore());
			result.setId(chapterStored.getId());
			result.setVersion(chapterStored.getVersion());
			result.setTitle(registrationForm.getTitle());
			result.setArea(chapterStored.getArea());

			this.validateEmail(registrationForm.getEmail(), binding);

			if (registrationForm.getUsername().isEmpty() && registrationForm.getPassword().isEmpty() && registrationForm.getConfirmPassword().isEmpty()) // No ha actualizado ningun atributo de user account
				result.setUserAccount(chapterStored.getUserAccount());
			else if (!registrationForm.getUsername().isEmpty() && registrationForm.getPassword().isEmpty() && registrationForm.getConfirmPassword().isEmpty()) {// Modifica el username
				this.validateUsernameEdition(registrationForm.getUsername(), binding);
				if (binding.hasErrors()) {

				} else {
					userAccount = chapterStored.getUserAccount();
					userAccount.setUsername(registrationForm.getUsername());
					result.setUserAccount(userAccount);
				}
			} else if (registrationForm.getUsername().isEmpty() && !registrationForm.getPassword().isEmpty() && !registrationForm.getConfirmPassword().isEmpty()) { // Modifica la password
				this.validatePasswordEdition(registrationForm.getPassword(), registrationForm.getConfirmPassword(), binding);
				if (binding.hasErrors()) {

				} else {
					userAccount = chapterStored.getUserAccount();
					userAccount.setPassword(registrationForm.getPassword());
					result.setUserAccount(userAccount);
				}
			} else if (!registrationForm.getUsername().isEmpty() && !registrationForm.getPassword().isEmpty() && !registrationForm.getConfirmPassword().isEmpty()) { // Modifica el username y la password
				this.validateUsernamePasswordEdition(registrationForm, binding);
				if (binding.hasErrors()) {

				} else {
					userAccount = chapterStored.getUserAccount();
					userAccount.setUsername(registrationForm.getUsername());
					userAccount.setPassword(registrationForm.getPassword());
					result.setUserAccount(userAccount);
				}
			}

		}
		this.validator.validate(result, binding);

		return result;
	}

	private void validateRegistration(final Chapter chapter, final ChapterRegistrationForm registrationForm, final BindingResult binding) {
		String password, confirmPassword, username;
		boolean checkBox, checkBoxData;

		password = registrationForm.getPassword();
		confirmPassword = registrationForm.getConfirmPassword();
		username = registrationForm.getUsername();
		checkBox = registrationForm.getCheckBoxAccepted();
		checkBoxData = registrationForm.getCheckBoxDataProcessesAccepted();

		this.validateEmail(chapter.getEmail(), binding);
		if (username.trim().equals(""))
			binding.rejectValue("username", "actor.username.blank", "Must entry a username.");
		if (password.trim().equals("") && confirmPassword.trim().equals("")) {
			binding.rejectValue("password", "password.empty", "Must entry a password");
			binding.rejectValue("confirmPassword", "confirmPassword.empty", "Must entry a confirm password");
		}
		if (!password.equals(confirmPassword))
			binding.rejectValue("confirmPassword", "user.missmatch.password", "Does not match with password");
		if (checkBox == false)
			binding.rejectValue("checkBoxAccepted", "actor.checkBox.agree", "Must agree terms and conditions and data processes");
		if (checkBoxData == false)
			binding.rejectValue("checkBoxDataProcessesAccepted", "actor.checkBoxData.agree", "Must agree data processes");
		if (this.userAccountService.existUsername(username))
			binding.rejectValue("username", "actor.username.used", "Username already in use");
		if (password.length() < 5 || password.length() > 32)
			binding.rejectValue("password", "actor.password.size", "Password must have between 5 and 32 characters");
		if (username.length() < 5 || username.length() > 32)
			binding.rejectValue("username", "actor.username.size", "Username must have between 5 and 32 characters.");
	}

	private void validateUsernamePasswordEdition(final ChapterRegistrationForm registrationForm, final BindingResult binding) {
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

	public ChapterRegistrationForm createForm(final Chapter chapter) {
		ChapterRegistrationForm chapterRegistrationForm;

		chapterRegistrationForm = new ChapterRegistrationForm();

		chapterRegistrationForm.setId(chapter.getId());
		chapterRegistrationForm.setName(chapter.getName());
		chapterRegistrationForm.setSurname(chapter.getSurname());
		chapterRegistrationForm.setAddress(chapter.getAddress());
		chapterRegistrationForm.setEmail(chapter.getEmail());
		chapterRegistrationForm.setMiddleName(chapter.getMiddleName());
		chapterRegistrationForm.setPhoto(chapter.getPhoto());
		chapterRegistrationForm.setTitle(chapter.getTitle());
		chapterRegistrationForm.setCheckBoxAccepted(false);
		chapterRegistrationForm.setCheckBoxDataProcessesAccepted(false);

		return chapterRegistrationForm;

	}

	// Req 8.1.3 Chapters coordinate at least 10% more parades than average
	public Collection<Chapter> chaptersCoordinateLeast10MoreParadasThanAverage() {
		Map<Chapter, Long> map;
		Double avg;

		avg = this.paradeService.avgNumberParadesCoordinatedByChapters();
		final List<Object[]> list = this.chapterRepository.chaptersCoordinateLeast10MoreParadasThanAverage(avg);
		Chapter key;
		Long value;
		final Collection<Chapter> result;

		map = new HashMap<>();
		for (final Object[] ob : list) {
			key = (Chapter) ob[0];
			value = (Long) ob[1];

			if (value != null)
				map.put(key, value);

		}

		result = map.keySet();

		return result;
	}

}
