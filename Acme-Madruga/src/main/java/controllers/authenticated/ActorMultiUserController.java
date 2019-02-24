
package controllers.authenticated;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import security.UserAccountService;
import services.ActorService;
import controllers.ActorAbstractController;
import domain.Actor;
import domain.Administrator;
import domain.Brotherhood;
import domain.Member;

@Controller
@RequestMapping(value = "/actor/administrator,brotherhood,member")
public class ActorMultiUserController extends ActorAbstractController {

	// Services

	@Autowired
	private ActorService		actorService;

	@Autowired
	private UserAccountService	userAccountService;


	// Constructor

	public ActorMultiUserController() {
		super();
	}

	// Edit

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int actorId) {
		ModelAndView result;
		Actor actor;

		try {
			actor = this.actorService.findOneToDisplayEdit(actorId);
			result = this.editModelAndView(actor);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/error.do");
		}

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveAdmin")
	public ModelAndView save(@Valid final Administrator actor, final BindingResult binding, final HttpServletRequest request) {
		ModelAndView result;
		String newUsername, newPassword, confirmPassword;

		confirmPassword = request.getParameter("confirmPassword");
		newPassword = request.getParameter("newPassword");
		newUsername = request.getParameter("newUsername");

		result = new ModelAndView();

		if (!newUsername.isEmpty() && newPassword.equals("d41d8cd98f00b204e9800998ecf8427e")) { // Solo ha modificado el username y la password la ha dejado en blanco
			if (this.userAccountService.existUsername(newUsername))
				this.editModelAndView(actor, "actor.username.used");
			else
				this.userAccountService.setLogin(actor.getUserAccount(), newUsername, actor.getUserAccount().getPassword());
		} else if (newUsername.isEmpty() && !newPassword.isEmpty() && !newPassword.equals("d41d8cd98f00b204e9800998ecf8427e")) {// Modifica la password solo
			if (newPassword.length() < 5 || newPassword.length() > 32)
				this.editModelAndView(actor, "actor.password.size");
			else if (!confirmPassword.equals(newPassword))
				this.editModelAndView(actor, "actor.password.missmatch");
			else
				this.userAccountService.setLogin(actor.getUserAccount(), actor.getUserAccount().getUsername(), newPassword);
		} else if (!newUsername.isEmpty() && !newPassword.equals("d41d8cd98f00b204e9800998ecf8427e")) { // Actualiza ambas cosas
			if (this.userAccountService.existUsername(newUsername))
				this.editModelAndView(actor, "actor.username.used");
			if (newPassword.length() < 5 || newPassword.length() > 32)
				this.editModelAndView(actor, "actor.password.size");
			else if (!confirmPassword.equals(newPassword))
				this.editModelAndView(actor, "actor.password.missmatch");
			else
				this.userAccountService.setLogin(actor.getUserAccount(), newUsername, newPassword);
		}

		if (binding.hasErrors())
			result = this.editModelAndView(actor);
		else
			try {
				this.actorService.save(actor);
				result = new ModelAndView("redirect:/actor/display.do");
			} catch (final Throwable oops) {
				result = this.editModelAndView(actor, "actor.commit.error");
			}

		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveBrotherhood")
	public ModelAndView save(@Valid final Brotherhood actor, final BindingResult binding, final HttpServletRequest request) {
		ModelAndView result;
		String newUsername, newPassword, confirmPassword;

		confirmPassword = request.getParameter("confirmPassword");
		newPassword = request.getParameter("newPassword");
		newUsername = request.getParameter("newUsername");

		result = new ModelAndView();

		if (!newUsername.isEmpty() && newPassword.equals("d41d8cd98f00b204e9800998ecf8427e")) { // Solo ha modificado el username y la password la ha dejado en blanco
			if (this.userAccountService.existUsername(newUsername))
				this.editModelAndView(actor, "actor.username.used");
			else
				this.userAccountService.setLogin(actor.getUserAccount(), newUsername, actor.getUserAccount().getPassword());
		} else if (newUsername.isEmpty() && !newPassword.isEmpty() && !newPassword.equals("d41d8cd98f00b204e9800998ecf8427e")) {// Modifica la password solo
			if (newPassword.length() < 5 || newPassword.length() > 32)
				this.editModelAndView(actor, "actor.password.size");
			else if (!confirmPassword.equals(newPassword))
				this.editModelAndView(actor, "actor.password.missmatch");
			else
				this.userAccountService.setLogin(actor.getUserAccount(), actor.getUserAccount().getUsername(), newPassword);
		} else if (!newUsername.isEmpty() && !newPassword.equals("d41d8cd98f00b204e9800998ecf8427e")) { // Actualiza ambas cosas
			if (this.userAccountService.existUsername(newUsername))
				this.editModelAndView(actor, "actor.username.used");
			if (newPassword.length() < 5 || newPassword.length() > 32)
				this.editModelAndView(actor, "actor.password.size");
			else if (!confirmPassword.equals(newPassword))
				this.editModelAndView(actor, "actor.password.missmatch");
			else
				this.userAccountService.setLogin(actor.getUserAccount(), newUsername, newPassword);
		}

		if (binding.hasErrors())
			result = this.editModelAndView(actor);
		else
			try {
				this.actorService.save(actor);
				result = new ModelAndView("redirect:/actor/display.do");
			} catch (final Throwable oops) {
				result = this.editModelAndView(actor, "actor.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveMember")
	public ModelAndView save(@Valid final Member actor, final BindingResult binding, final HttpServletRequest request) {
		ModelAndView result;
		String newUsername, newPassword, confirmPassword;

		confirmPassword = request.getParameter("confirmPassword");
		newPassword = request.getParameter("newPassword");
		newUsername = request.getParameter("newUsername");

		result = new ModelAndView();

		if (!newUsername.isEmpty() && newPassword.equals("d41d8cd98f00b204e9800998ecf8427e")) { // Solo ha modificado el username y la password la ha dejado en blanco
			if (this.userAccountService.existUsername(newUsername))
				this.editModelAndView(actor, "actor.username.used");
			else
				this.userAccountService.setLogin(actor.getUserAccount(), newUsername, actor.getUserAccount().getPassword());
		} else if (newUsername.isEmpty() && !newPassword.isEmpty() && !newPassword.equals("d41d8cd98f00b204e9800998ecf8427e")) {// Modifica la password solo
			if (newPassword.length() < 5 || newPassword.length() > 32)
				this.editModelAndView(actor, "actor.password.size");
			else if (!confirmPassword.equals(newPassword))
				this.editModelAndView(actor, "actor.password.missmatch");
			else
				this.userAccountService.setLogin(actor.getUserAccount(), actor.getUserAccount().getUsername(), newPassword);
		} else if (!newUsername.isEmpty() && !newPassword.equals("d41d8cd98f00b204e9800998ecf8427e")) { // Actualiza ambas cosas
			if (this.userAccountService.existUsername(newUsername))
				this.editModelAndView(actor, "actor.username.used");
			if (newPassword.length() < 5 || newPassword.length() > 32)
				this.editModelAndView(actor, "actor.password.size");
			else if (!confirmPassword.equals(newPassword))
				this.editModelAndView(actor, "actor.password.missmatch");
			else
				this.userAccountService.setLogin(actor.getUserAccount(), newUsername, newPassword);
		}

		if (binding.hasErrors())
			result = this.editModelAndView(actor);
		else
			try {
				this.actorService.save(actor);
				result = new ModelAndView("redirect:/actor/display.do");
			} catch (final Throwable oops) {
				result = this.editModelAndView(actor, "actor.commit.error");
			}

		return result;
	}

	// Ancillary methods

	protected ModelAndView editModelAndView(final Actor actor) {
		ModelAndView result;

		result = this.editModelAndView(actor, null);

		return result;
	}

	protected ModelAndView editModelAndView(final Actor actor, final String messageCode) {
		ModelAndView result;
		String role;

		role = "";

		for (final Authority a : this.userAccountService.findByActor(actor).getAuthorities())
			switch (a.toString()) {
			case Authority.ADMIN:
				role = "administrator";
				break;
			case Authority.BROTHERHOOD:
				role = "brotherhood";
				break;
			case Authority.MEMBER:
				role = "member";
				break;
			default:
				break;
			}

		result = new ModelAndView("actor/edit");
		result.addObject("actor", actor);
		result.addObject("role", role);
		result.addObject("messageCode", messageCode);

		return result;
	}
}
