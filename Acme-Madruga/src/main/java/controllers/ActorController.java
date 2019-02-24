
package controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Brotherhood;
import domain.Member;

@Controller
@RequestMapping(value = "/actor")
public class ActorController extends ActorAbstractController {

	// Constructor

	public ActorController() {
		super();
	}

	// Creation

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final String role) {
		Assert.isTrue(role.equals("brotherhood") || role.equals("member"));
		final ModelAndView result;

		result = this.createActor(role);
		result.addObject("Url", "actor/");

		return result;
	}

	// Display

	@Override
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam(required = false) final Integer actorId) {
		ModelAndView result;

		try {
			result = super.display(actorId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/error.do");
		}

		return result;
	}

	// Register

	@RequestMapping(value = "/registerbrotherhood", method = RequestMethod.POST, params = "save")
	public ModelAndView registerBrotherhood(@Valid final Brotherhood brotherhood, final BindingResult binding, final HttpServletRequest request) {

		ModelAndView result;

		result = this.registerActor(brotherhood, binding, request);
		result.addObject("Url", "actor/");

		return result;
	}

	@RequestMapping(value = "/registermember", method = RequestMethod.POST, params = "save")
	public ModelAndView registerMember(@Valid final Member member, final BindingResult binding, final HttpServletRequest request) {

		ModelAndView result;

		result = this.registerActor(member, binding, request);
		result.addObject("Url", "actor/");

		return result;
	}

}
