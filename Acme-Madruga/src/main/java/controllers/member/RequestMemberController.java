
package controllers.member;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.MemberService;
import services.ProcessionService;
import services.RequestService;
import controllers.AbstractController;
import domain.Procession;
import domain.Request;

@Controller
@RequestMapping(value = "/request/member")
public class RequestMemberController extends AbstractController {

	@Autowired
	private RequestService		requestService;

	@Autowired
	private ProcessionService	processionService;

	@Autowired
	private MemberService		memberService;


	// Constructors -----------------------------------------------------------

	public RequestMemberController() {
		super();
	}

	// Request List -----------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		Collection<Request> pendingRequests;
		Collection<Request> approvedRequests;
		Collection<Request> rejectedRequests;

		Integer memberId;

		pendingRequests = this.requestService.findPendingRequestByMember();
		approvedRequests = this.requestService.findApprovedRequestByMember();
		rejectedRequests = this.requestService.findRejectedRequestByMember();
		memberId = this.memberService.findByPrincipal().getId();

		result = new ModelAndView("request/list");
		result.addObject("pendingRequests", pendingRequests);
		result.addObject("approvedRequests", approvedRequests);
		result.addObject("rejectedRequests", rejectedRequests);
		result.addObject("memberId", memberId);
		result.addObject("requestURI", "request/member/list.do");

		return result;
	}

	// Request create -----------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int processionId) {
		ModelAndView result;
		final Request request;

		try {
			try {
				final Procession procession;

				procession = this.processionService.findOne(processionId);
				request = this.requestService.create(procession);
				this.requestService.saveNew(request);
				result = new ModelAndView("redirect:/request/member/list.do");
			} catch (final Throwable oops) {
				result = new ModelAndView("redirect:../../error.do");

			}
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}
		return result;
	}

	//Delete
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int requestId) {
		ModelAndView result;
		Request request;

		try {

			request = this.requestService.findOneToMember(requestId);

			try {
				this.requestService.deleteCancel(request);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(request, "request.commit.error");
			}

		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

	// Arcillary methods --------------------------

	protected ModelAndView createEditModelAndView(final Request request) {
		ModelAndView result;

		result = this.createEditModelAndView(request, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Request request, final String messageCode) {
		ModelAndView result;
		int memberId;

		memberId = this.memberService.findByPrincipal().getId();

		result = new ModelAndView("request/edit");
		result.addObject("request", request);
		result.addObject("messageCode", messageCode);
		result.addObject("memberId", memberId);

		return result;

	}
}
