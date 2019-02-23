/*
 * ProfileController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.brotherhoodMember;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.BrotherhoodService;
import services.RequestService;
import controllers.AbstractController;
import domain.Request;

@Controller
@RequestMapping("/request/brotherhoodMember")
public class RequestBrotherhoodMemberController extends AbstractController {

	@Autowired
	private RequestService		requestService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Constructors -----------------------------------------------------------

	public RequestBrotherhoodMemberController() {
		super();
	}

	// Request List -----------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int requestId) {
		ModelAndView result;
		Request request;
		String rolActor;

		try {
			result = new ModelAndView("request/display");
			if (LoginService.getPrincipal().getAuthorities().toString().equals("[MEMBER]")) {
				request = this.requestService.findOneToMember(requestId);
				rolActor = "member";
			} else {
				request = this.requestService.findOneToBrotherhood(requestId);
				rolActor = "brotherhood";
			}
			result.addObject("request", request);
			result.addObject("rolActor", rolActor);
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

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
}
