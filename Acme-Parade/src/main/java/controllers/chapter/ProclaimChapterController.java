
package controllers.chapter;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ProclaimService;
import controllers.AbstractController;
import domain.Proclaim;

@Controller
@RequestMapping("/proclaim/chapter")
public class ProclaimChapterController extends AbstractController {

	@Autowired
	private ProclaimService	proclaimService;


	public ProclaimChapterController() {
		super();
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Proclaim proclaim;

		proclaim = this.proclaimService.create();

		result = this.createModelAndView(proclaim);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Proclaim proclaim, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createModelAndView(proclaim);
		else
			try {
				this.proclaimService.save(proclaim);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				result = this.createModelAndView(proclaim, "proclaim.commit.error");
			}

		return result;
	}

	// Ancillary methods ------------------------
	protected ModelAndView createModelAndView(final Proclaim proclaim) {
		ModelAndView result;

		result = this.createModelAndView(proclaim, null);

		return result;
	}

	protected ModelAndView createModelAndView(final Proclaim proclaim, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("proclaim/edit");
		result.addObject("proclaim", proclaim);
		result.addObject("messageCode", messageCode);

		return result;
	}

}
