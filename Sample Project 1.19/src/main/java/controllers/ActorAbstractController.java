
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CustomisationService;
import domain.Actor;
import domain.Administrator;

@Controller
public class ActorAbstractController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private ActorService			actorService;

	@Autowired
	private CustomisationService	customisationService;


	// Main methods -----------------------------------------------------------

	// Display --------------------------------------------------------------------

	public ModelAndView display(final Integer actorId) {
		ModelAndView result;
		Actor actor, principal;

		actor = null;
		principal = null;
		try {
			principal = this.actorService.findPrincipal();
		} catch (final Throwable oops) {

		}
		result = new ModelAndView("actor/display");

		if (actorId == null) {
			actor = this.actorService.findPrincipal();
			result.addObject("isAuthorized", true);
			result.addObject("isActorLogged", true);
		} else {
			actor = this.actorService.findOne(actorId);
			if (actor instanceof Administrator && actorId == principal.getId())
				actor = this.actorService.findOneToDisplayEdit(actorId);
			else if (actor instanceof Administrator && actorId != principal.getId())
				throw new IllegalArgumentException();

		}

		if (principal != null && actor != null && principal == actor) {
			result.addObject("isActorLogged", true);
			result.addObject("isAuthorized", true);
		}

		result.addObject("actor", actor);
		result.addObject("thresholdScore", this.customisationService.find().getThresholdScore());

		return result;
	}
	// Ancillary methods ------------------------------------------------------

}
