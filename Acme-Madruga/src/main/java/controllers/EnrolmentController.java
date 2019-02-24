/*
 * CustomerController.java
 * 
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.Collection;
import java.util.Locale;
import java.util.SortedMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.EnrolmentService;
import services.PositionService;
import domain.Enrolment;
import domain.Position;

@Controller
@RequestMapping("enrolment/")
public class EnrolmentController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private EnrolmentService	enrolmentService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private PositionService		positionService;


	// Constructors -----------------------------------------------------------

	public EnrolmentController() {
		super();
	}

	// Controller methods -----------------------------------------------------		

	@RequestMapping(value = "/listMember", method = RequestMethod.GET)
	public ModelAndView listMember(@RequestParam final int brotherhoodId, final Locale locale) {
		ModelAndView result;
		Collection<Enrolment> enrolments;
		boolean isOwnBrotherhood;
		SortedMap<Integer, String> positionMap;
		Collection<Position> positions;

		positions = this.positionService.findAll();
		positionMap = this.positionService.positionsByLanguages(positions, locale.getLanguage());
		enrolments = this.enrolmentService.findEnroledMembers(brotherhoodId);

		try {
			isOwnBrotherhood = this.brotherhoodService.findByPrincipal().getId() == brotherhoodId;
		} catch (final Throwable oops) {
			isOwnBrotherhood = false;
		}

		result = new ModelAndView("enrolment/memberList");
		result.addObject("requestURI", "enrolment/listMember.do");
		result.addObject("enrolments", enrolments);
		result.addObject("positions", positionMap);
		result.addObject("isOwnBrotherhood", isOwnBrotherhood);

		return result;
	}

}
