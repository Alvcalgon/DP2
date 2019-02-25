
package controllers.authenticated;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.SocialProfileService;
import controllers.AbstractController;
import domain.Actor;
import domain.SocialProfile;
import forms.SocialProfileForm;

@Controller
@RequestMapping(value = "/socialProfile/administrator,brotherhood,member")
public class SocialProfileMultiUserController extends AbstractController {

	@Autowired
	private SocialProfileService	socialProfileService;

	@Autowired
	private ActorService			actorService;


	// Constructors -----------------------------------------------------------

	public SocialProfileMultiUserController() {
		super();
	}

	// Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		SocialProfileForm socialProfileForm;

		try {
			socialProfileForm = new SocialProfileForm();

			result = this.createEditModelAndView(socialProfileForm);

			return result;
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

	// Edit

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int socialProfileId) {
		ModelAndView result;
		SocialProfileForm socialProfileForm;
		SocialProfile socialProfile;

		try {
			socialProfile = this.socialProfileService.findOneToEdit(socialProfileId);

			socialProfileForm = new SocialProfileForm();
			socialProfileForm.setId(socialProfileId);
			socialProfileForm.setLinkProfile(socialProfile.getLinkProfile());
			socialProfileForm.setNick(socialProfile.getNick());
			socialProfileForm.setSocialNetwork(socialProfile.getSocialNetwork());
			result = this.createEditModelAndView(socialProfileForm);
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final SocialProfileForm socialProfileForm, final BindingResult binding) {
		ModelAndView result;
		Actor actor;
		SocialProfile socialProfile;

		this.socialProfileService.validateLinkProfile(socialProfileForm, binding);
		this.socialProfileService.validateLinkProfileUnique(socialProfileForm, binding);
		this.socialProfileService.validateNick(socialProfileForm, binding);
		this.socialProfileService.validateSocialNetwork(socialProfileForm, binding);
		try {
			actor = this.actorService.findPrincipal();

			if (binding.hasErrors())
				result = this.createEditModelAndView(socialProfileForm);
			else
				try {
					socialProfile = this.socialProfileService.reconstruct(socialProfileForm, binding);
					this.socialProfileService.save(socialProfile);
					result = new ModelAndView("redirect:../../socialProfile/list.do?actorId=" + actor.getId());
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(socialProfileForm, "socialProfile.commit.error");
				}
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int socialProfileId) {
		ModelAndView result;
		Actor actor;
		SocialProfile socialProfile;

		try {
			actor = this.actorService.findPrincipal();
			socialProfile = this.socialProfileService.findOneToEdit(socialProfileId);

			try {
				this.socialProfileService.delete(socialProfile);
				result = new ModelAndView("redirect:../../socialProfile/list.do?actorId=" + actor.getId());
			} catch (final Throwable oops) {
				//	result = this.createEditModelAndView(socialProfile, "socialProfile.commit.error");
				result = new ModelAndView("redirect:../../error.do");
			}
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

	// Arcillary methods --------------------------

	protected ModelAndView createEditModelAndView(final SocialProfileForm socialProfileForm) {
		ModelAndView result;

		result = this.createEditModelAndView(socialProfileForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final SocialProfileForm socialProfileForm, final String messageCode) {
		ModelAndView result;
		Actor principal;
		int actorId;

		principal = this.actorService.findPrincipal();
		actorId = principal.getId();

		result = new ModelAndView("socialProfile/edit");
		result.addObject("socialProfileForm", socialProfileForm);
		result.addObject("messageCode", messageCode);
		result.addObject("actorId", actorId);

		return result;

	}

}
