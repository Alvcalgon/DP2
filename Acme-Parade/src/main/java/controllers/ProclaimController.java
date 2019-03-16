
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ProclaimService;
import domain.Proclaim;

@Controller
@RequestMapping("/proclaim")
public class ProclaimController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private ProclaimService	proclaimService;


	// Constructors -----------------------------------------------------------

	public ProclaimController() {
		super();
	}

	// Controller methods -----------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int chapterId) {
		ModelAndView result;
		Collection<Proclaim> proclaims;

		proclaims = this.proclaimService.findByChapterId(chapterId);

		result = new ModelAndView("proclaim/list");
		result.addObject("proclaims", proclaims);
		result.addObject("requestURI", "proclaim/list.do");

		return result;
	}

}
