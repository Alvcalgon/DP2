/*
 * CustomerController.java
 * 
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.member;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import services.EnrolmentService;
import controllers.AbstractController;
import domain.Enrolment;

@Controller
@RequestMapping("enrolment/member")
public class EnrolmentMemberController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private EnrolmentService	enrolmentService;


	// Constructors -----------------------------------------------------------

	public EnrolmentMemberController() {
		super();
	}

	// Controller methods -----------------------------------------------------		

	@RequestMapping(value = "/requestEnrolment", method = RequestMethod.GET)
	public ModelAndView requestEnrolment(@RequestParam final int brotherhoodId) {
		ModelAndView result;

		this.enrolmentService.requestEnrolment(brotherhoodId);
		result = new ModelAndView("redirect:/actor/display.do?actorId=" + brotherhoodId);

		return result;
	}

	@RequestMapping(value = "/listBrotherhood", method = RequestMethod.GET)
	public ModelAndView listBrotherhood() {
		ModelAndView result;
		Collection<Enrolment> enrolments;

		enrolments = this.enrolmentService.findAllEnrolmentsByPrincipal();

		result = new ModelAndView("enrolment/brotherhoodList");
		result.addObject("enrolments", enrolments);

		return result;
	}

	@RequestMapping(value = "/dropOut", method = RequestMethod.GET)
	public ModelAndView dropOut(@RequestParam final int brotherhoodId, final RedirectAttributes redir) {
		ModelAndView result;
		Enrolment enrolment;

		try {
			enrolment = this.enrolmentService.findByBrotherhoodId(brotherhoodId);
			this.enrolmentService.dropOut(enrolment);
		} catch (final Throwable oops) {
			redir.addFlashAttribute("messageCode", "actor.commit.error");
		}

		result = new ModelAndView("redirect:/actor/display.do?actorId=" + brotherhoodId);

		return result;
	}

}
