
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.EnrolmentService;
import domain.Actor;
import domain.Administrator;
import domain.Brotherhood;
import domain.Member;

@Controller
public class ActorAbstractController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private EnrolmentService	enrolmentService;


	// Main methods -----------------------------------------------------------

	// Display --------------------------------------------------------------------

	public ModelAndView display(final Integer actorId) {
		ModelAndView result;
		Actor actor, principal;
		Brotherhood brotherhood;
		boolean isEnrolled, existEnrolmentRequest;
		final boolean hasSelectedArea;

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

		if (principal != null && actor != null && principal instanceof Member && actor instanceof Brotherhood) {
			isEnrolled = this.enrolmentService.findIsEnrolledIn(principal.getId(), actorId);
			existEnrolmentRequest = this.enrolmentService.findExistRequestEnrolment(principal.getId(), actorId);
			brotherhood = (Brotherhood) actor;
			hasSelectedArea = brotherhood.getArea() != null;

			result.addObject("isEnrolled", isEnrolled);
			result.addObject("hasSelectedArea", hasSelectedArea);
			result.addObject("existEnrolmentRequest", existEnrolmentRequest);
		}

		if (principal != null && actor != null && principal == actor) {
			result.addObject("isActorLogged", true);
			result.addObject("isAuthorized", true);
		}

		result.addObject("actor", actor);

		return result;
	}
	// Ancillary methods ------------------------------------------------------

}
