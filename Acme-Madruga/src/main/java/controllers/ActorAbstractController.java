
package controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import security.UserAccount;
import security.UserAccountService;
import services.ActorService;
import services.AdministratorService;
import services.BrotherhoodService;
import services.MemberService;
import domain.Actor;
import domain.Administrator;
import domain.Brotherhood;
import domain.Member;

@Controller
public class ActorAbstractController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private ActorService			actorService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private MemberService			memberService;

	@Autowired
	private UserAccountService		userAccountService;


	// Main methods -----------------------------------------------------------

	public ModelAndView createActor(final String role) {
		ModelAndView result;
		Actor actor;

		switch (role) {
		case "administrator":
			actor = this.administratorService.create();
			result = this.createModelAndView(actor, role);
			break;
		case "brotherhood":
			actor = this.brotherhoodService.create();
			result = this.createModelAndView(actor, role);
			break;
		case "member":
			actor = this.memberService.create();
			result = this.createModelAndView(actor, role);
			break;
		default:
			result = new ModelAndView("redirect:/welcome/index.do");
			result.addObject("messageCode", "actor.commit.error");
			break;
		}

		return result;

	}

	public ModelAndView registerActor(final Actor actor, final BindingResult binding, final HttpServletRequest request) {
		ModelAndView result;
		String username, password, role, confirmPassword;
		final Administrator administrator;
		final Brotherhood brotherhood;
		final Member member;
		UserAccount userAccount;

		username = request.getParameter("username");
		password = request.getParameter("password");
		confirmPassword = request.getParameter("confirmPassword");
		role = request.getParameter("role");

		if (binding.hasErrors())
			result = this.createModelAndView(actor, role);
		else if (!confirmPassword.equals(password))
			result = this.createModelAndView(actor, role, "actor.password.missmatch");
		else if (this.userAccountService.existUsername(username))
			result = this.createModelAndView(actor, role, "actor.username.used");
		else if (this.actorService.existEmail(actor.getEmail()))
			result = this.createModelAndView(actor, role, "actor.email.used");
		else if (password.length() < 5 || password.length() > 32)
			result = this.createModelAndView(actor, role, "actor.password.size");
		else if (username.length() < 5 || username.length() > 32)
			result = this.createModelAndView(actor, role, "actor.username.size");
		else
			try {

				this.userAccountService.setLogin(actor.getUserAccount(), username, password);
				userAccount = actor.getUserAccount();

				userAccount.setIsBanned(false);

				switch (role) {
				case "administrator":
					administrator = (Administrator) actor;
					this.administratorService.save(administrator);
					break;
				case "brotherhood":
					brotherhood = (Brotherhood) actor;
					this.brotherhoodService.save(brotherhood);
					break;
				case "member":
					member = (Member) actor;
					this.memberService.save(member);
					break;
				default:
					throw new Throwable();
				}

				result = new ModelAndView("redirect:/welcome/index.do");
				result.addObject("messageCode", "actor.registration.successful");
			} catch (final Throwable oops) {
				result = this.createModelAndView(actor, role, "actor.commit.error");
			}

		return result;
	}

	// Display --------------------------------------------------------------------

	public ModelAndView display(final Integer actorId) {
		ModelAndView result;
		Actor actor;

		actor = null;
		result = new ModelAndView("actor/display");

		if (actorId == null) {
			actor = this.actorService.findPrincipal();
			result.addObject("isAuthorized", true);
		} else {
			actor = this.actorService.findOne(actorId);
			result.addObject("isAuthorized", false);
		}

		result.addObject("actor", actor);

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createModelAndView(final Actor actor, final String role) {
		ModelAndView result;

		result = this.createModelAndView(actor, role, null);

		return result;
	}

	protected ModelAndView createModelAndView(final Actor actor, final String role, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/singup");
		result.addObject("role", role);
		result.addObject(role, actor);

		result.addObject("messageCode", messageCode);

		return result;
	}

}