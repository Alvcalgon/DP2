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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import services.AreaService;
import services.FinderService;
import controllers.AbstractController;
import domain.Finder;

@Controller
@RequestMapping("finder/member/")
public class FinderMemberController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private FinderService	finderService;

	@Autowired
	private AreaService		areaService;


	// Constructors -----------------------------------------------------------

	public FinderMemberController() {
		super();
	}

	// Controller methods -----------------------------------------------------		

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Finder finder;

		finder = this.finderService.findByMemberPrincipal();
		result = this.createEditModelAndView(finder);

		return result;
	}

	@RequestMapping(value = "/clear", method = RequestMethod.GET)
	public ModelAndView clear() {
		ModelAndView result;
		Finder finder;

		finder = this.finderService.findByMemberPrincipal();
		this.finderService.clear(finder);

		result = new ModelAndView("redirect:/procession/member/listFinder.do");

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Finder finder, final BindingResult binding, final RedirectAttributes redir) {
		ModelAndView result;

		finder = this.finderService.reconstruct(finder, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(finder);
		else
			try {
				this.finderService.save(finder);
			} catch (final Throwable oops) {
				redir.addFlashAttribute("messageCode", "finder.commit.error");
			}

		result = new ModelAndView("redirect:/procession/member/listFinder.do");

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Finder finder) {
		ModelAndView result;

		result = this.createEditModelAndView(finder, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Finder finder, final String messageCode) {
		ModelAndView result;
		Collection<String> areas;

		areas = this.areaService.findAllAreaNames();

		result = new ModelAndView("finder/edit");
		result.addObject("finder", finder);
		result.addObject("areas", areas);
		result.addObject("messageCode", messageCode);

		return result;
	}

}
