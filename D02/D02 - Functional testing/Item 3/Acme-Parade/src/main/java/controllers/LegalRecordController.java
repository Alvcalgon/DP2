
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.HistoryService;
import services.LegalRecordService;
import domain.History;
import domain.LegalRecord;

@Controller
@RequestMapping(value = "/legalRecord")
public class LegalRecordController extends AbstractController {

	@Autowired
	private LegalRecordService	legalRecordService;

	@Autowired
	private HistoryService		historyService;


	// Constructors -----------------------------------------------------------

	public LegalRecordController() {
		super();
	}

	// Float display ---------------------------------------------------------------		

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int legalRecordId) {
		ModelAndView result;
		LegalRecord legalRecord;
		History history;

		result = new ModelAndView("legalRecord/display");

		try {
			legalRecord = this.legalRecordService.findOne(legalRecordId);
			history = this.historyService.findHistoryByLegalRecord(legalRecordId);

			result.addObject("legalRecord", legalRecord);
			result.addObject("brotherhoodId", history.getBrotherhood().getId());

		} catch (final Exception e) {
			result = new ModelAndView("redirect:../error.do");
		}

		return result;
	}
}
