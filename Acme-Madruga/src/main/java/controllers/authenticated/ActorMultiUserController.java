
package controllers.authenticated;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AdministratorService;
import services.BrotherhoodService;
import services.MemberService;
import controllers.ActorAbstractController;
import domain.Actor;
import domain.Administrator;
import domain.Brotherhood;
import domain.Member;
import forms.BrotherhoodRegistrationForm;
import forms.RegistrationForm;

@Controller
@RequestMapping(value = "/actor/administrator,brotherhood,member")
public class ActorMultiUserController extends ActorAbstractController {

	// Services

	@Autowired
	private ActorService			actorService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private MemberService			memberService;


	// Constructor

	public ActorMultiUserController() {
		super();
	}

	// Edit

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int actorId) {
		ModelAndView result;
		Actor actor;
		final Brotherhood brotherhood;
		final RegistrationForm registrationForm;
		final BrotherhoodRegistrationForm brotherhoodRegistrationForm;

		try {
			actor = this.actorService.findOneToDisplayEdit(actorId);
			if (actor instanceof Member || actor instanceof Administrator) {
				registrationForm = new RegistrationForm();
				registrationForm.setId(actor.getId());
				registrationForm.setName(actor.getName());
				registrationForm.setMiddleName(actor.getMiddleName());
				registrationForm.setSurname(actor.getSurname());
				registrationForm.setPhoto(actor.getPhoto());
				registrationForm.setEmail(actor.getEmail());
				registrationForm.setPhoneNumber(actor.getPhoneNumber());
				registrationForm.setAddress(actor.getAddress());

				result = this.createModelAndView(registrationForm);
				if (actor instanceof Member)
					result.addObject("rol", "Member");
				else
					result.addObject("rol", "Administrator");
			} else {
				brotherhood = this.brotherhoodService.findOneToDisplayEdit(actorId);
				brotherhoodRegistrationForm = new BrotherhoodRegistrationForm();
				brotherhoodRegistrationForm.setId(brotherhood.getId());
				brotherhoodRegistrationForm.setName(brotherhood.getName());
				brotherhoodRegistrationForm.setMiddleName(brotherhood.getMiddleName());
				brotherhoodRegistrationForm.setSurname(brotherhood.getSurname());
				brotherhoodRegistrationForm.setPhoto(brotherhood.getPhoto());
				brotherhoodRegistrationForm.setEmail(brotherhood.getEmail());
				brotherhoodRegistrationForm.setPhoneNumber(brotherhood.getPhoneNumber());
				brotherhoodRegistrationForm.setAddress(brotherhood.getAddress());
				brotherhoodRegistrationForm.setTitle(brotherhood.getTitle());
				brotherhoodRegistrationForm.setEstablishmentDate(brotherhood.getEstablishmentDate());
				brotherhoodRegistrationForm.setPictures(brotherhood.getPictures());

				result = this.createModelAndView(brotherhoodRegistrationForm);
				result.addObject("rol", "Brotherhood");
			}

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/error.do");
		}

		return result;

	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveAdmin")
	public ModelAndView saveAdministrator(final RegistrationForm registrationForm, final BindingResult binding) {
		ModelAndView result;
		Administrator administrator;
		administrator = null;

		this.administratorService.validateName(registrationForm, binding);
		this.administratorService.validateSurname(registrationForm, binding);
		this.administratorService.validateEmail(registrationForm, binding);

		if (!binding.hasErrors())
			administrator = this.administratorService.reconstruct(registrationForm, binding);

		if (binding.hasErrors()) {
			result = this.createModelAndView(registrationForm);
			result.addObject("rol", "Administrator");
		} else
			try {
				this.administratorService.save(administrator);
				result = new ModelAndView("redirect:/actor/display.do");
			} catch (final Throwable oops) {
				result = this.createModelAndView(registrationForm, "actor.commit.error");
			}

		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveBrotherhood")
	public ModelAndView saveBrotherhood(@ModelAttribute("registrationForm") final BrotherhoodRegistrationForm brotherhoodRegistrationForm, final BindingResult binding) {
		ModelAndView result;
		Brotherhood brotherhood;
		brotherhood = null;

		this.brotherhoodService.validateName(brotherhoodRegistrationForm, binding);
		this.brotherhoodService.validateSurname(brotherhoodRegistrationForm, binding);
		this.brotherhoodService.validateEmail(brotherhoodRegistrationForm, binding);
		this.brotherhoodService.validateTitle(brotherhoodRegistrationForm, binding);
		this.brotherhoodService.validateTitle(brotherhoodRegistrationForm, binding);

		if (!binding.hasErrors())
			brotherhood = this.brotherhoodService.reconstruct(brotherhoodRegistrationForm, binding);

		if (binding.hasErrors()) {
			result = this.createModelAndView(brotherhoodRegistrationForm);
			result.addObject("rol", "Brotherhood");
		} else
			try {
				this.brotherhoodService.save(brotherhood);
				result = new ModelAndView("redirect:/actor/display.do");
			} catch (final Throwable oops) {
				result = this.createModelAndView(brotherhoodRegistrationForm, "actor.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveMember")
	public ModelAndView saveMember(final RegistrationForm registrationForm, final BindingResult binding) {
		ModelAndView result;
		Member member;
		member = null;

		this.memberService.validateName(registrationForm, binding);
		this.memberService.validateSurname(registrationForm, binding);
		this.memberService.validateEmail(registrationForm, binding);

		if (!binding.hasErrors())
			member = this.memberService.reconstruct(registrationForm, binding);

		if (binding.hasErrors()) {
			result = this.createModelAndView(registrationForm);
			result.addObject("rol", "Member");
		} else
			try {
				this.memberService.save(member);
				result = new ModelAndView("redirect:/actor/display.do");
			} catch (final Throwable oops) {
				result = this.createModelAndView(registrationForm, "actor.commit.error");
			}

		return result;
	}

	// Ancillary methods

	protected ModelAndView createModelAndView(final RegistrationForm registrationForm) {
		ModelAndView result;

		result = this.createModelAndView(registrationForm, null);

		return result;
	}

	protected ModelAndView createModelAndView(final RegistrationForm registrationForm, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/edit");
		result.addObject("registrationForm", registrationForm);
		result.addObject("messageCode", messageCode);

		return result;
	}

	protected ModelAndView createModelAndView(final BrotherhoodRegistrationForm brotherhoodRegistrationForm) {
		ModelAndView result;

		result = this.createModelAndView(brotherhoodRegistrationForm, null);

		return result;
	}

	protected ModelAndView createModelAndView(final BrotherhoodRegistrationForm brotherhoodRegistrationForm, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/edit");
		result.addObject("registrationForm", brotherhoodRegistrationForm);
		result.addObject("messageCode", messageCode);
		result.addObject("role", "brotherhood");

		return result;
	}

}
