/*
 * CustomerController.java
 * 
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.brotherhood;

import java.util.Collection;
import java.util.Locale;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import services.EnrolmentService;
import services.PositionService;
import controllers.AbstractController;
import domain.Enrolment;
import domain.Position;

@Controller
@RequestMapping("enrolment/brotherhood/")
public class EnrolmentBrotherhoodController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private EnrolmentService	enrolmentService;

	@Autowired
	private PositionService		positionService;


	// Constructors -----------------------------------------------------------

	public EnrolmentBrotherhoodController() {
		super();
	}

	// Controller methods -----------------------------------------------------		

	@RequestMapping(value = "/listMemberRequest", method = RequestMethod.GET)
	public ModelAndView listMemberRequest() {
		ModelAndView result;
		Collection<Enrolment> enrolments;

		enrolments = this.enrolmentService.findRequestEnrolments();

		result = new ModelAndView("enrolment/requestList");
		result.addObject("requestURI", "enrolment/brotherhood/listMemberRequest.do");
		result.addObject("enrolments", enrolments);
		result.addObject("isRequestList", true);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int enrolmentId, final Locale locale) {
		ModelAndView result;
		Enrolment enrolment;

		try {
			enrolment = this.enrolmentService.findOneToEdit(enrolmentId);
			result = this.createEditModelAndView(enrolment, locale);
		} catch (final Throwable oops) {
			result = new ModelAndView("/error.do");
		}

		return result;
	}

	@RequestMapping(value = "/enrol", method = RequestMethod.GET)
	public ModelAndView enrol(@RequestParam final int enrolmentId, final Locale locale, final RedirectAttributes redir) {
		ModelAndView result;

		// TODO: comprobar si al llamar enrol, el idioma se sigue manteniendo (inglés y español)
		result = new ModelAndView("redirect:edit.do?enrolmentId=" + enrolmentId);
		redir.addFlashAttribute("isEnrolling", true);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(Enrolment enrolment, final BindingResult binding, final Locale locale, final HttpServletRequest request) {
		ModelAndView result;
		Enrolment saved;
		boolean isEnrolling;
		String debugging;

		debugging = request.getParameter("position");
		System.out.println(debugging);
		enrolment = this.enrolmentService.reconstruct(enrolment, binding);
		isEnrolling = this.stringToBoolean(request.getParameter("isEnrolling"));
		// TODO: pasar la transformacion string-boolean a createEditModelAndView?
		if (binding.hasErrors())
			result = this.createEditModelAndView(enrolment, isEnrolling, locale);
		else
			try {
				if (isEnrolling) {
					saved = this.enrolmentService.saveToEditPosition(enrolment);
					result = new ModelAndView("redirect:/enrolment/brotherhood/listMemberRequest.do");
				} else {
					saved = this.enrolmentService.enrol(enrolment);
					result = new ModelAndView("redirect:/enrolment/listMember.do?brotherhoodId=" + saved.getBrotherhood().getId());
				}
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(enrolment, isEnrolling, locale, "enrolment.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/reject", method = RequestMethod.GET)
	public ModelAndView reject(final int enrolmentId, final RedirectAttributes redir) {
		ModelAndView result;
		Enrolment enrolment;

		try {
			enrolment = this.enrolmentService.findOne(enrolmentId);
			this.enrolmentService.reject(enrolment);
		} catch (final Throwable oops) {
			redir.addFlashAttribute("messageCode", "enrolment.commit.error");
		}

		result = new ModelAndView("redirect:listMemberRequest.do");

		return result;
	}

	@RequestMapping(value = "/remove", method = RequestMethod.GET)
	public ModelAndView remove(final int enrolmentId, final RedirectAttributes redir) {
		ModelAndView result;
		Enrolment enrolment;

		enrolment = null;

		try {
			enrolment = this.enrolmentService.findOne(enrolmentId);
			this.enrolmentService.remove(enrolment);
		} catch (final Throwable oops) {
			redir.addFlashAttribute("messageCode", "enrolment.commit.error");
		}

		if (enrolment != null)
			result = new ModelAndView("redirect:/enrolment/listMember.do?brotherhoodId=" + enrolment.getBrotherhood().getId());
		else
			result = new ModelAndView("redirect:/welcome/index.do");

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Enrolment enrolment, final Locale locale) {
		ModelAndView result;

		result = this.createEditModelAndView(enrolment, locale, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Enrolment enrolment, final boolean isEnrolling, final Locale locale) {
		ModelAndView result;

		result = this.createEditModelAndView(enrolment, locale, null);
		result.addObject("isEnrolling", isEnrolling);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Enrolment enrolment, final boolean isEnrolling, final Locale locale, final String messageCode) {
		ModelAndView result;

		result = this.createEditModelAndView(enrolment, locale, messageCode);
		result.addObject("isEnrolling", isEnrolling);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Enrolment enrolment, final Locale locale, final String messageCode) {
		ModelAndView result;
		SortedMap<Integer, String> positionMap;
		Collection<Position> positions;

		positions = this.positionService.findAll();
		positionMap = this.positionService.positionsByLanguages(positions, locale.getLanguage());

		result = new ModelAndView("enrolment/edit");
		result.addObject("enrolment", enrolment);
		result.addObject("memberName", enrolment.getMember().getFullname());
		result.addObject("positions", positionMap);
		result.addObject("messageCode", messageCode);

		return result;
	}

	private boolean stringToBoolean(final String bool) {
		return bool.equals("true");
	}

}
