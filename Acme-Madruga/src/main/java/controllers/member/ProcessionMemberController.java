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
import org.springframework.web.servlet.ModelAndView;

import services.CustomisationService;
import services.FinderService;
import controllers.AbstractController;
import domain.Customisation;
import domain.Finder;

@Controller
@RequestMapping("procession/member/")
public class ProcessionMemberController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private FinderService			finderService;

	@Autowired
	private CustomisationService	customisationService;


	// Constructors -----------------------------------------------------------

	public ProcessionMemberController() {
		super();
	}

	// Controller methods -----------------------------------------------------		

	@RequestMapping(value = "/listFinder", method = RequestMethod.GET)
	public ModelAndView listFinder() {
		ModelAndView result;
		Customisation customisation;
		int numberOfResults;
		Finder finder;

		customisation = this.customisationService.find();
		numberOfResults = customisation.getMaxNumberResults();
		finder = this.finderService.findByMemberPrincipal();
		finder = this.finderService.evaluateSearch(finder);

		result = new ModelAndView("procession/listFinder");
		result.addObject("requestURI", "procession/member/listFinder.do");
		result.addObject("finder", finder);
		result.addObject("processions", finder.getProcessions());
		result.addObject("numberOfResults", numberOfResults);

		return result;
	}

}
