
package controllers.administrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.SponsorshipService;
import controllers.AbstractController;

@Controller
@RequestMapping("sponsorship/administrator")
public class SponsorshipAdministratorController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private SponsorshipService	sponsorshipService;


	// Constructors -----------------------------------------------------------

	public SponsorshipAdministratorController() {
		super();
	}

	// Controller methods -----------------------------------------------------		

	@RequestMapping(value = "/process/deactivate", method = RequestMethod.GET)
	public ModelAndView deactivateProcess() {
		ModelAndView result;

		try {
			this.sponsorshipService.deactivateProcess();
			result = new ModelAndView("process/launcher");
			result.addObject("messageCode", "process.success");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/error.do");
			result.addObject("messageCode", "process.error");
		}

		return result;
	}
}
