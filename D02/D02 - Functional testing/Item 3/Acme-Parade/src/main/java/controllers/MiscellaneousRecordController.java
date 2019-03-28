
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.HistoryService;
import services.MiscellaneousRecordService;
import domain.History;
import domain.MiscellaneousRecord;

@Controller
@RequestMapping(value = "/miscellaneousRecord")
public class MiscellaneousRecordController extends AbstractController {

	@Autowired
	private MiscellaneousRecordService	miscellaneousRecordService;

	@Autowired
	private HistoryService				historyService;


	// Constructors -----------------------------------------------------------

	public MiscellaneousRecordController() {
		super();
	}

	// Float display ---------------------------------------------------------------		

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int miscellaneousRecordId) {
		ModelAndView result;
		MiscellaneousRecord miscellaneousRecord;
		History history;

		result = new ModelAndView("miscellaneousRecord/display");

		try {
			miscellaneousRecord = this.miscellaneousRecordService.findOne(miscellaneousRecordId);
			history = this.historyService.findHistoryByMiscellaneousRecord(miscellaneousRecordId);

			result.addObject("miscellaneousRecord", miscellaneousRecord);
			result.addObject("brotherhoodId", history.getBrotherhood().getId());

		} catch (final Exception e) {
			result = new ModelAndView("redirect:../error.do");
		}

		return result;
	}
}
