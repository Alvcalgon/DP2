
package controllers.chapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

		parade = this.paradeService.findOne(paradeId);
		brotherhoodId = this.brotherhoodService.findBrotherhoodByParade(paradeId).getId();

		try {
			this.paradeService.accept(parade);
			result = new ModelAndView("redirect:/parade/list.do?brotherhoodId=" + brotherhoodId);
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

}
