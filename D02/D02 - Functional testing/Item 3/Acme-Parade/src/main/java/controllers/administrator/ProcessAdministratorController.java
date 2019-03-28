
package controllers.administrator;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;

@Controller
@RequestMapping("process/administrator")
public class ProcessAdministratorController extends AbstractController {

	// Services ---------------------------------------------------------------

	// Constructors -----------------------------------------------------------

	public ProcessAdministratorController() {
		super();
	}

	// Controller methods -----------------------------------------------------		

	@RequestMapping(value = "/launcher/display", method = RequestMethod.GET)
	public ModelAndView displayLauncher() {
		ModelAndView result;

		result = new ModelAndView("process/launcher");

		return result;
	}
}
