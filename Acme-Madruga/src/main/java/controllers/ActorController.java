
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AdministratorService;
import services.AreaService;
import services.BrotherhoodService;
import services.MemberService;
import domain.Area;
import domain.Brotherhood;
import domain.Member;
import forms.BrotherhoodRegistrationForm;
import forms.RegistrationForm;

@Controller
@RequestMapping(value = "/actor")
public class ActorController extends ActorAbstractController {

	// Services

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private MemberService			memberService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private AreaService				areaService;


	// Constructor

	public ActorController() {
		super();
	}

	// Display

	@Override
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam(required = false) final Integer actorId) {
		ModelAndView result;

		try {
			result = super.display(actorId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/error.do");
		}

		return result;
	}

	// Register

	@RequestMapping(value = "/registerBrotherhood", method = RequestMethod.GET)
	public ModelAndView createBrotherhood() {
		ModelAndView result;
		String rol;
		Brotherhood brotherhood;

		rol = "Brotherhood";
		brotherhood = new Brotherhood();
		result = this.createModelAndView(brotherhood);
		result.addObject("rol", rol);
		result.addObject("urlAdmin", "");

		return result;
	}

	@RequestMapping(value = "/registerBrotherhood", method = RequestMethod.POST, params = "save")
	public ModelAndView saveBrotherhood(@ModelAttribute("registrationForm") final BrotherhoodRegistrationForm registrationForm, final BindingResult binding) {
		ModelAndView result;
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.reconstruct(registrationForm, binding);

		if (binding.hasErrors()) {
			result = this.createModelAndView(registrationForm);
			result.addObject("rol", "Brotherhood");
		} else
			try {
				this.brotherhoodService.save(brotherhood);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				result = this.createModelAndView(registrationForm, "actor.registration.error");
			}

		return result;
	}

	@RequestMapping(value = "/registerMember", method = RequestMethod.GET)
	public ModelAndView createMember() {
		ModelAndView result;
		String rol;
		Member member;

		rol = "Member";
		member = new Member();
		result = this.createModelAndView(member);
		result.addObject("rol", rol);
		result.addObject("urlAdmin", "");

		return result;
	}

	@RequestMapping(value = "/registerMember", method = RequestMethod.POST, params = "save")
	public ModelAndView saveMember(final RegistrationForm registrationForm, final BindingResult binding) {
		ModelAndView result;
		Member member;

		member = this.memberService.reconstruct(registrationForm, binding);

		if (binding.hasErrors()) {
			result = this.createModelAndView(registrationForm);
			result.addObject("rol", "Member");
		} else
			try {
				this.memberService.save(member);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				result = this.createModelAndView(registrationForm, "actor.registration.error");
			}

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createModelAndView(final Member member) {
		ModelAndView result;
		RegistrationForm registrationForm;

		registrationForm = this.memberService.createForm(member);

		result = this.createModelAndView(registrationForm, null);

		return result;
	}

	protected ModelAndView createModelAndView(final Brotherhood brotherhood) {
		ModelAndView result;
		BrotherhoodRegistrationForm brotherhoodRegistrationForm;

		brotherhoodRegistrationForm = this.brotherhoodService.createForm(brotherhood);

		result = this.createModelAndView(brotherhoodRegistrationForm, null);

		return result;
	}

	protected ModelAndView createModelAndView(final RegistrationForm registrationForm) {
		ModelAndView result;

		result = this.createModelAndView(registrationForm, null);

		return result;
	}

	protected ModelAndView createModelAndView(final RegistrationForm registrationForm, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/singup");
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
		Collection<Area> areas;

		areas = this.areaService.findAll();

		result = new ModelAndView("actor/singup");
		result.addObject("registrationForm", brotherhoodRegistrationForm);
		result.addObject("messageCode", messageCode);
		result.addObject("role", "brotherhood");
		result.addObject("areas", areas);

		return result;
	}

}
