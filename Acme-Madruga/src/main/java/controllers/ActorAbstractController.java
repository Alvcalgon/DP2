
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.EnrolmentService;
import domain.Actor;
import domain.Administrator;
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
		boolean isEnrolled;
		boolean existEnrolmentRequest;

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
		} else {
			actor = this.actorService.findOne(actorId);
			if (actor instanceof Administrator && actorId == principal.getId())
				actor = this.actorService.findOneToDisplayEdit(actorId);
			else if (actor instanceof Administrator && actorId != principal.getId())
				throw new IllegalArgumentException();
			result.addObject("isAuthorized", false);
		}

		if (principal != null && principal instanceof Member) {
			isEnrolled = this.enrolmentService.findIsEnrolledIn(principal.getId(), actorId);
			existEnrolmentRequest = this.enrolmentService.findExistEnrolmentRequestOf(principal.getId(), actorId);

			result.addObject("isEnrolled", isEnrolled);
			result.addObject("existEnrolmentRequest", existEnrolmentRequest);
		}

		result.addObject("actor", actor);

		return result;
	}
	// Ancillary methods ------------------------------------------------------

}
