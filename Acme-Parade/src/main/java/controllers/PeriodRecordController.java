
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.HistoryService;
import services.PeriodRecordService;
import services.UtilityService;
import domain.History;
import domain.PeriodRecord;

@Controller
@RequestMapping(value = "/periodRecord")
public class PeriodRecordController extends AbstractController {

	@Autowired
	private PeriodRecordService	periodRecordService;

	@Autowired
	private UtilityService		utilityService;

	@Autowired
	private HistoryService		historyService;


	// Constructors -----------------------------------------------------------

	public PeriodRecordController() {
		super();
	}

	// Float display ---------------------------------------------------------------		

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int periodRecordId) {
		ModelAndView result;
		PeriodRecord periodRecord;
		Collection<String> photos;
		History history;

		result = new ModelAndView("periodRecord/display");

		try {
			periodRecord = this.periodRecordService.findOne(periodRecordId);
			photos = this.utilityService.getSplittedString(periodRecord.getPhotos());
			history = this.historyService.findHistoryByPeriodRecord(periodRecordId);

			result.addObject("periodRecord", periodRecord);
			result.addObject("brotherhoodId", history.getBrotherhood().getId());
			result.addObject("photos", photos);

		} catch (final Exception e) {
			result = new ModelAndView("redirect:../error.do");
		}

		return result;
	}
}
