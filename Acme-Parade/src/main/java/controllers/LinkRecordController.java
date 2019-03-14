
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.HistoryService;
import services.LinkRecordService;
import domain.History;
import domain.LinkRecord;

@Controller
@RequestMapping(value = "/linkRecord")
public class LinkRecordController extends AbstractController {

	@Autowired
	private LinkRecordService	linkRecordService;

	@Autowired
	private HistoryService		historyService;


	// Constructors -----------------------------------------------------------

	public LinkRecordController() {
		super();
	}

	// Float display ---------------------------------------------------------------		

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int linkRecordId) {
		ModelAndView result;
		LinkRecord linkRecord;
		History history;

		result = new ModelAndView("linkRecord/display");

		try {
			linkRecord = this.linkRecordService.findOne(linkRecordId);
			history = this.historyService.findHistoryByLinkRecord(linkRecordId);

			result.addObject("linkRecord", linkRecord);
			result.addObject("brotherhoodId", history.getBrotherhood().getId());

		} catch (final Exception e) {
			result = new ModelAndView("redirect:../error.do");
		}

		return result;
	}
}
