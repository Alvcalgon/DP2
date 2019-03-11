
package controllers.sponsor;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.SponsorshipService;
import controllers.AbstractController;
import domain.Sponsorship;

@Controller
@RequestMapping("sponsorship/sponsor")
public class SponsorshipSponsorController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private SponsorshipService	sponsorshipService;


	// Constructors -----------------------------------------------------------

	public SponsorshipSponsorController() {
		super();
	}

	// Controller methods -----------------------------------------------------		

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Sponsorship> sponsorships;

		try {
			sponsorships = this.sponsorshipService.findAllByPrincipal();

			result = new ModelAndView("sponsorship/list");
			result.addObject("sponsorships", sponsorships);
			result.addObject("requestURI", "sponsorship/sponsor/list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/error.do");
		}

		return result;
	}

}
