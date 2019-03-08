
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AdministratorService;
import controllers.ActorAbstractController;
import domain.Actor;
import domain.Administrator;
import forms.RegistrationForm;

@Controller
@RequestMapping(value = "/actor/administrator")
public class ActorAdministratorController extends ActorAbstractController {

	// Services

	@Autowired
	private ActorService			actorService;

	@Autowired
	private AdministratorService	administratorService;


	// Constructors

	public ActorAdministratorController() {
		super();
	}

	// List

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Actor> actors;

		actors = this.actorService.findAll();

		result = new ModelAndView("actor/list");

		result.addObject("actors", actors);
		result.addObject("requestURI", "actor/administrator/list.do");
		return result;
	}

	// Ban

	@RequestMapping(value = "/changeBan", method = RequestMethod.GET)
	public ModelAndView changeBan(@RequestParam final int actorId) {
		ModelAndView result;
		Actor actor;

		actor = this.actorService.findOne(actorId);

		try {
			this.actorService.changeBan(actor);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:list.do");
		}

		return result;
	}

	// Score

	@RequestMapping(value = "/computeScore", method = RequestMethod.POST, params = "compute")
	public ModelAndView computeScore() {
		ModelAndView result;

		try {
			this.actorService.scoreProcess();
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/error.do");
		}

		return result;
	}

	// Spammers

	@RequestMapping(value = "/spammersProcess", method = RequestMethod.POST, params = "spammers")
	public ModelAndView spammersProcess() {
		ModelAndView result;

		try {
			this.actorService.spammerProcess();
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/error.do");
		}

		result = new ModelAndView("redirect:list.do");

		return result;
	}

	@RequestMapping(value = "/registerAdministrator", method = RequestMethod.GET)
	public ModelAndView createAdministrator() {
		ModelAndView result;
		String rol;
		final Administrator administrator;

		rol = "Administrator";
		administrator = new Administrator();
		result = this.createModelAndView(administrator);
		result.addObject("rol", rol);
		result.addObject("urlAdmin", "administrator/");

		return result;
	}

	@RequestMapping(value = "/registerAdministrator", method = RequestMethod.POST, params = "save")
	public ModelAndView saveAdministrator(final RegistrationForm registrationForm, final BindingResult binding) {
		ModelAndView result;
		Administrator administrator;

		administrator = this.administratorService.reconstruct(registrationForm, binding);

		if (binding.hasErrors()) {
			result = this.createModelAndView(registrationForm);
			result.addObject("rol", "Administrator");
		} else
			try {
				this.administratorService.save(administrator);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				result = this.createModelAndView(registrationForm, "actor.registration.error");
				result.addObject("rol", "Administrator");
			}

		return result;
	}

	// Ancillary Methods

	protected ModelAndView createModelAndView(final Administrator administrator) {
		ModelAndView result;
		RegistrationForm registrationForm;

		registrationForm = this.administratorService.createForm(administrator);

		result = this.createModelAndView(registrationForm, null);

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
		result.addObject("urlAdmin", "administrator/");

		return result;
	}

}
