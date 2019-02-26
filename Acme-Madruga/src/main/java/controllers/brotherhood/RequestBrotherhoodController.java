
package controllers.brotherhood;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.CustomisationService;
import services.RequestService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Request;

@Controller
@RequestMapping(value = "/request/brotherhood")
public class RequestBrotherhoodController extends AbstractController {

	@Autowired
	private RequestService			requestService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private CustomisationService	customisationService;


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
		try {
			request = this.requestService.findOneToBrotherhood(requestId);

			try {
				this.requestService.saveEditRejected(request);
				result = new ModelAndView("redirect:edit.do?requestId=" + requestId);
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(request, "request.commit.error");
			}
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}
		return result;
	}

	//Accept
	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public ModelAndView accept(@RequestParam final int requestId) {
		ModelAndView result;
		Request request;

		try {
			request = this.requestService.findOneToBrotherhood(requestId);

			try {
				this.requestService.saveEditApproved(request);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(request, "request.commit.error");
			}
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

	//Edit
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int requestId) {
		ModelAndView result;
		Request request;

		try {
			request = this.requestService.findOneToBrotherhood(requestId);
			Assert.notNull(request);
			result = this.createEditModelAndView(request);
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

	//Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Request request, final BindingResult binding) {
		ModelAndView result;
		Brotherhood brotherhood;

		try {
			brotherhood = this.brotherhoodService.findByPrincipal();

			if (binding.hasErrors())
				result = this.createEditModelAndView(request);
			else
				try {
					this.requestService.saveEdit(request);
					result = new ModelAndView("redirect:../../request/brotherhood/list.do");
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
		int brotherhoodId;

		brotherhoodId = this.brotherhoodService.findByPrincipal().getId();

		result = new ModelAndView("request/edit");
		result.addObject("request", request);
		result.addObject("messageCode", messageCode);
		result.addObject("brotherhoodId", brotherhoodId);

		return result;

	}
}
