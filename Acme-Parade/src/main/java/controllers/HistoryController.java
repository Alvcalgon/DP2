
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.HistoryService;
import domain.History;

@Controller
@RequestMapping("/history")
public class HistoryController extends AbstractController {

	// Services --------------------------------------------------

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private HistoryService		historyService;


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

		try {
			brotherhoodLoginId = this.brotherhoodService.findByPrincipal().getId();
		} catch (final Throwable ups) {
		}
		history = this.historyService.findOne(this.historyService.findHistoryByBrotherhood(brotherhoodId).getId());

		result = new ModelAndView("history/display");
		result.addObject("history", history);
		result.addObject("brotherhoodHistoryId", brotherhoodId);
		result.addObject("requestURI", "history/display.do");
		if (brotherhoodLoginId != null)
			result.addObject("brotherhoodLoginId", brotherhoodLoginId);

		return result;
	}
}
