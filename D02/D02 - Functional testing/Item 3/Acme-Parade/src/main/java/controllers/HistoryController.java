
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.HistoryService;
import services.UtilityService;
import domain.History;

@Controller
@RequestMapping("/history")
public class HistoryController extends AbstractController {

	// Services --------------------------------------------------

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private HistoryService		historyService;

	@Autowired
	private UtilityService		utilityService;


	// Constructor

	public HistoryController() {
		super();
	}

	// Creating ---------------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int brotherhoodId) {
		ModelAndView result;
		History history;
		Integer brotherhoodLoginId = null;
		Collection<String> photos;

		result = new ModelAndView("history/display");

		try {
			try {
				brotherhoodLoginId = this.brotherhoodService.findByPrincipal().getId();
			} catch (final Exception e) {
			}

			history = this.historyService.findOne(this.historyService.findHistoryByBrotherhood(brotherhoodId).getId());
			photos = this.utilityService.getSplittedString(history.getInceptionRecord().getPhotos());

			result.addObject("history", history);
			result.addObject("brotherhoodHistoryId", brotherhoodId);
			result.addObject("requestURI", "history/display.do");
			result.addObject("photos", photos);
			if (brotherhoodLoginId != null)
				result.addObject("brotherhoodLoginId", brotherhoodLoginId);

		} catch (final Exception e) {
			result = new ModelAndView("redirect:../error.do");
		}

		return result;
	}
}
