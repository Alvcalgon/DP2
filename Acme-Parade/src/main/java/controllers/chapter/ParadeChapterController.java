
package controllers.chapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.ParadeService;
import controllers.AbstractController;
import domain.Parade;

@Controller
@RequestMapping("parade/chapter/")
public class ParadeChapterController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private ParadeService		paradeService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Constructors -----------------------------------------------------------

	public ParadeChapterController() {
		super();
	}

	// Controller methods -----------------------------------------------------		

	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public ModelAndView accept(@RequestParam final int paradeId) {
		ModelAndView result;
		Parade parade;
		int brotherhoodId;

		try {
			parade = this.paradeService.findOneToEditChapter(paradeId);
			brotherhoodId = this.brotherhoodService.findBrotherhoodByParade(paradeId).getId();

			this.paradeService.accept(parade);

			result = new ModelAndView("redirect:/parade/list.do?brotherhoodId=" + brotherhoodId);
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

	@RequestMapping(value = "/reject", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int paradeId) {
		ModelAndView result;
		Parade parade;

		try {
			parade = this.paradeService.findOneToEditChapter(paradeId);

			result = this.createEditModelAndView(parade);
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

	@RequestMapping(value = "/reject", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Parade parade, final BindingResult binding) {
		ModelAndView result;
		Parade paradeRec;

		paradeRec = this.paradeService.reconstruct(parade, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(parade);
		else
			try {
				this.paradeService.saveRejected(paradeRec);
				result = new ModelAndView("redirect:/parade/chapter/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(paradeRec, "parade.commit.error");
			}

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Parade parade) {
		ModelAndView result;

		result = this.createEditModelAndView(parade, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Parade parade, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("parade/reject");
		result.addObject("parade", parade);
		result.addObject("messageCode", messageCode);

		return result;
	}

}
