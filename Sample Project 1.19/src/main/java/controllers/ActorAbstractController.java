
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CustomisationService;
import services.EnrolmentService;
import services.UtilityService;
import domain.Actor;
import domain.Administrator;
import domain.Brotherhood;
import domain.Member;

@Controller
public class ActorAbstractController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private ActorService			actorService;

	@Autowired
	private EnrolmentService		enrolmentService;

	@Autowired
	private CustomisationService	customisationService;

	@Autowired
	private UtilityService			utilityService;


	// Main methods -----------------------------------------------------------

	// Display --------------------------------------------------------------------

	public ModelAndView display(final Integer actorId) {
		ModelAndView result;
		Actor actor, principal;
		Brotherhood brotherhood;
		boolean isEnrolled, existEnrolmentRequest;
		final boolean hasSelectedArea;
		Collection<String> pictures;

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
			if (actor instanceof Brotherhood) {
				pictures = this.utilityService.getSplittedString(((Brotherhood) actor).getPictures());
				result.addObject("pictures", pictures);
			}
		} else {
			actor = this.actorService.findOne(actorId);
			if (actor instanceof Administrator && actorId == principal.getId())
				actor = this.actorService.findOneToDisplayEdit(actorId);
			else if (actor instanceof Administrator && actorId != principal.getId())
				throw new IllegalArgumentException();
			else if (actor instanceof Brotherhood) {
				pictures = this.utilityService.getSplittedString(((Brotherhood) actor).getPictures());
				result.addObject("pictures", pictures);
			}

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

		if (principal != null && actor != null && principal instanceof Brotherhood && actor instanceof Member) {
			isEnrolled = this.enrolmentService.findIsEnrolledIn(actor.getId(), principal.getId());
			if (isEnrolled == true)
				result.addObject("memberEnrolled", isEnrolled);
			else
				result.addObject("memberEnrolled", isEnrolled);
		}

		result.addObject("actor", actor);
		result.addObject("thresholdScore", this.customisationService.find().getThresholdScore());

		return result;
	}
	// Ancillary methods ------------------------------------------------------

}
