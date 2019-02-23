
package controllers.brotherhood;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.ProcessionService;
import services.RequestService;
import controllers.AbstractController;
import domain.Request;

@Controller
@RequestMapping(value = "/request/brotherhood")
public class RequestBrotherhoodController extends AbstractController {

	@Autowired
	private RequestService		requestService;

	@Autowired
	private ProcessionService	processionService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Constructors -----------------------------------------------------------

	public RequestBrotherhoodController() {
		super();
	}

	// Request List -----------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		Collection<Request> pendingRequests;
		Collection<Request> approvedRequests;
		Collection<Request> rejectedRequests;

		Integer brotherhoodId;

		pendingRequests = this.requestService.findPendingRequestByBrotherhood();
		approvedRequests = this.requestService.findApprovedRequestByBrotherhood();
		rejectedRequests = this.requestService.findRejectedRequestByBrotherhood();
		brotherhoodId = this.brotherhoodService.findByPrincipal().getId();

		result = new ModelAndView("request/list");
		result.addObject("pendingRequests", pendingRequests);
		result.addObject("approvedRequests", approvedRequests);
		result.addObject("rejectedRequests", rejectedRequests);
		result.addObject("brotherhoodId", brotherhoodId);
		result.addObject("requestURI", "request/brotherhood/list.do");

		return result;
	}

	//Reject
	@RequestMapping(value = "/reject", method = RequestMethod.GET)
	public ModelAndView reject(@RequestParam final int requestId) {
		ModelAndView result;
		Request request;

		request = this.requestService.findOneToBrotherhood(requestId);

		try {
			this.requestService.saveEditRejected(request);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(request, "request.commit.error");
		}

		return result;
	}

	//Accept
	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public ModelAndView accept(@RequestParam final int requestId) {
		ModelAndView result;
		Request request;

		request = this.requestService.findOneToBrotherhood(requestId);

		try {
			this.requestService.saveEditApproved(request);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(request, "request.commit.error");
		}

		return result;
	}

	// Request create -----------------------------------------------------------

	// Arcillary methods --------------------------

	protected ModelAndView createEditModelAndView(final Request request) {
		ModelAndView result;

		result = this.createEditModelAndView(request, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Request request, final String messageCode) {
		ModelAndView result;
		int brotherhoodId;

		brotherhoodId = this.brotherhoodService.findByPrincipal().getId();

		result = new ModelAndView("request/edit");
		result.addObject("request", request);
		result.addObject("messageCode", messageCode);
		result.addObject("brotherhoodId", brotherhoodId);

		return result;

	}
}