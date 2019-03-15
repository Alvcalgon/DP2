
package controllers.sponsor;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CustomisationService;
import services.SponsorshipService;
import services.UtilityService;
import controllers.AbstractController;
import domain.Customisation;
import domain.Sponsorship;

@Controller
@RequestMapping("sponsorship/sponsor")
public class SponsorshipSponsorController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private SponsorshipService		sponsorshipService;

	@Autowired
	private CustomisationService	customisationService;

	@Autowired
	private UtilityService			utilityService;


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

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int sponsorshipId) {
		ModelAndView result;
		Sponsorship sponsorship;

		try {
			sponsorship = this.sponsorshipService.findOneToEditDisplay(sponsorshipId);

			result = new ModelAndView("sponsorship/display");
			result.addObject("sponsorship", sponsorship);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/error.do");
		}

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int paradeId) {
		ModelAndView result;
		Sponsorship sponsorship;

		try {
			sponsorship = this.sponsorshipService.create(paradeId);
			result = this.createEditModelAndView(sponsorship);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/error.do");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int sponsorshipId) {
		ModelAndView result;
		Sponsorship sponsorship;

		try {
			sponsorship = this.sponsorshipService.findOneToEditDisplay(sponsorshipId);
			result = this.createEditModelAndView(sponsorship);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/error.do");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Sponsorship sponsorship, final BindingResult binding) {
		ModelAndView result;
		Sponsorship sponsorshipRec, saved;

		sponsorshipRec = this.sponsorshipService.reconstruct(sponsorship, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(sponsorship);
		else
			try {
				saved = this.sponsorshipService.save(sponsorshipRec);
				result = new ModelAndView("redirect:display.do?sponsorshipId=" + saved.getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(sponsorshipRec, "sponsorship.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/remove", method = RequestMethod.GET)
	public ModelAndView remove(@RequestParam final int sponsorshipId) {
		ModelAndView result;
		Sponsorship sponsorship;

		try {
			sponsorship = this.sponsorshipService.findOne(sponsorshipId);
			this.sponsorshipService.removeBySponsor(sponsorship);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/error.do");
		}

		return result;
	}

	@RequestMapping(value = "/reactivate", method = RequestMethod.GET)
	public ModelAndView reactivate(@RequestParam final int sponsorshipId) {
		ModelAndView result;
		Sponsorship sponsorship;

		try {
			sponsorship = this.sponsorshipService.findOne(sponsorshipId);
			this.sponsorshipService.reactivate(sponsorship);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/error.do");
		}

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Sponsorship sponsorship) {
		ModelAndView result;

		result = this.createEditModelAndView(sponsorship, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Sponsorship sponsorship, final String messageCode) {
		ModelAndView result;
		Collection<String> makes;
		Customisation customisation;

		customisation = this.customisationService.find();
		makes = this.utilityService.ListByString(customisation.getCreditCardMakes());

		result = new ModelAndView("sponsorship/edit");
		result.addObject("sponsorship", sponsorship);
		result.addObject("makes", makes);
		result.addObject("messageCode", messageCode);

		return result;
	}

}
