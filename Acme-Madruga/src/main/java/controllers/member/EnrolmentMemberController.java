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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.EnrolmentService;
import controllers.AbstractController;

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

}
