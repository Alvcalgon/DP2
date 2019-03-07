
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

import services.BrotherhoodService;
import services.EnrolmentService;
import services.MemberService;
import services.PositionService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Enrolment;
import domain.Member;
import domain.Position;

@Controller
@RequestMapping("enrolment/brotherhood/")
public class EnrolmentBrotherhoodController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private EnrolmentService	enrolmentService;

	@Autowired
	private MemberService		memberService;

	@Autowired
	private PositionService		positionService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Constructors -----------------------------------------------------------

	public EnrolmentBrotherhoodController() {
		super();
	}

	// Controller methods -----------------------------------------------------		

	@RequestMapping(value = "/listMemberRequest", method = RequestMethod.GET)
	public ModelAndView listMemberRequest() {
		ModelAndView result;
		Collection<Enrolment> enrolments;
		Brotherhood brotherhood;

		enrolments = this.enrolmentService.findRequestEnrolments();
		brotherhood = this.brotherhoodService.findByPrincipal();

		result = new ModelAndView("enrolment/requestList");
		result.addObject("requestURI", "enrolment/brotherhood/listMemberRequest.do");
		result.addObject("enrolments", enrolments);
		result.addObject("isRequestList", true);
		result.addObject("brotherhoodId", brotherhood.getId());
		result.addObject("areaSelected", brotherhood.getArea() != null);

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
			result = new ModelAndView("redirect:/error.do");
		}

		return result;
	}

	@RequestMapping(value = "/enrol", method = RequestMethod.GET)
	public ModelAndView enrol(@RequestParam final int enrolmentId, final RedirectAttributes redir) {
		ModelAndView result;

		redir.addFlashAttribute("isEnrolling", true);
		result = new ModelAndView("redirect:edit.do?enrolmentId=" + enrolmentId);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(final Enrolment enrolment, final BindingResult binding, final Locale locale, final HttpServletRequest request) {
		ModelAndView result;
		Enrolment saved, enrolmentRec;
		boolean isEnrolling;

		enrolmentRec = this.enrolmentService.reconstruct(enrolment, binding);
		isEnrolling = this.stringToBoolean(request.getParameter("isEnrolling"));

		if (binding.hasErrors())
			result = this.createEditModelAndView(enrolment, isEnrolling, locale);
		else
			try {
				if (!isEnrolling) {
					saved = this.enrolmentService.saveToEditPosition(enrolmentRec);
					result = new ModelAndView("redirect:/enrolment/listMember.do?brotherhoodId=" + saved.getBrotherhood().getId());
				} else {
					saved = this.enrolmentService.enrol(enrolmentRec);
					result = new ModelAndView("redirect:/enrolment/brotherhood/listMemberRequest.do");
				}
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(enrolmentRec, isEnrolling, locale, "enrolment.commit.error");
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
		Member member;
		SortedMap<Integer, String> positionMap;
		Collection<Position> positions;

		positions = this.positionService.findAll();
		positionMap = this.positionService.positionsByLanguages(positions, locale.getLanguage());
		member = this.memberService.findByEnrolmentId(enrolment.getId());
		// We need this query because Enrolment is a pruned object and, in some cases, enrolment.getMember() == null

		result = new ModelAndView("enrolment/edit");
		result.addObject("enrolment", enrolment);
		result.addObject("memberName", member.getFullname());
		result.addObject("positions", positionMap);
		result.addObject("messageCode", messageCode);

		return result;
	}

	private boolean stringToBoolean(final String bool) {
		return bool.equals("true");
	}

}
