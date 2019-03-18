
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ParadeService;
import services.SegmentService;
import domain.Parade;
import domain.Segment;

@Controller
@RequestMapping(value = "/segment")
public class SegmentController extends AbstractController {

	@Autowired
	private SegmentService	segmentService;

	@Autowired
	private ParadeService	paradeService;


	// Constructors -----------------------------------------------------------

	public SegmentController() {
		super();
	}

	// Parade display ---------------------------------------------------------------		

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int segmentId) {
		ModelAndView result;
		Segment segment;
		Parade parade;

		result = new ModelAndView("segment/display");

		try {
			segment = this.segmentService.findOne(segmentId);
			parade = this.paradeService.findBySegment(segment.getId());

			result.addObject("segment", segment);
			result.addObject("paradeId", parade.getId());

		} catch (final Exception e) {

			result = new ModelAndView("redirect:../error.do");
		}

		return result;
	}

}
