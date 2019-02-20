
package controllers;

import org.displaytag.pagination.PaginatedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.SocialProfileService;
import utilities.internal.PaginatedListAdapter;
import domain.SocialProfile;

@Controller
@RequestMapping(value = "/socialProfile")
public class SocialProfileController extends AbstractController {

	@Autowired
	private SocialProfileService	socialProfileService;

	@Autowired
	private ActorService			actorService;


	// Constructors -----------------------------------------------------------

	public SocialProfileController() {
		super();
	}

	// List

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int actorId, @RequestParam(defaultValue = "1", required = false) final int page, @RequestParam(required = false) final String sort, @RequestParam(required = false) final String dir) {
		final ModelAndView result;
		final Page<SocialProfile> socialProfiles;
		Integer actorAuthenticateId = null;
		Pageable pageable;
		PaginatedList socialProfilesAdapted;

		pageable = this.newFixedPageable(page, dir, sort);
		socialProfiles = this.socialProfileService.findSocialProfilesByActor(actorId, pageable);
		socialProfilesAdapted = new PaginatedListAdapter(socialProfiles, sort);

		try {
			actorAuthenticateId = this.actorService.findPrincipal().getId();
		} catch (final Throwable ups) {
		}

		result = new ModelAndView("socialProfile/list");
		result.addObject("socialProfiles", socialProfilesAdapted);
		result.addObject("actorId", actorId);

		result.addObject("requestURI", "socialProfile/list.do?actorId=" + actorId);
		if (actorAuthenticateId != null) {
			if (actorAuthenticateId == actorId)
				result.addObject("isAuthorized", true);
			else
				result.addObject("isAuthorized", false);
		} else
			result.addObject("isAuthorized", false);
		return result;

	}
}
