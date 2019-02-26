
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import controllers.ActorAbstractController;
import domain.Actor;

@Controller
@RequestMapping(value = "/actor/administrator")
public class ActorAdministratorController extends ActorAbstractController {

	// Services

	@Autowired
	private ActorService	actorService;


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
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/error.do");
		}

		result = new ModelAndView("redirect:list.do");

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

}
