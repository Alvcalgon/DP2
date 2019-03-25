
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import security.UserAccount;
import services.BrotherhoodService;
import services.ChapterService;
import services.ParadeService;
import services.SegmentService;
import domain.Brotherhood;
import domain.Parade;
import domain.Segment;

@Controller
@RequestMapping(value = "/segment")
public class SegmentController extends AbstractController {

	@Autowired
	private SegmentService		segmentService;

	@Autowired
	private ParadeService		paradeService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private ChapterService		chapterService;


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
		UserAccount userPrincipal;
		Brotherhood brotherhood;

		result = new ModelAndView("segment/display");

		try {
			parade = this.paradeService.findBySegment(segmentId);
			brotherhood = this.brotherhoodService.findBrotherhoodByParade(parade.getId());
			try {
				userPrincipal = LoginService.getPrincipal();
			} catch (final Exception e1) {
				userPrincipal = null;
			}
			if (userPrincipal != null && userPrincipal.getAuthorities().toString().equals("[BROTHERHOOD]") && brotherhood.getId() == this.brotherhoodService.findByPrincipal().getId())
				segment = this.segmentService.findOne(segmentId);
			else if (userPrincipal != null && userPrincipal.getAuthorities().toString().equals("[CHAPTER]") && brotherhood.getArea() == this.chapterService.findByPrincipal().getArea())
				segment = this.segmentService.findOneToDisplayChapter(segmentId);
			else
				segment = this.segmentService.findOneToDisplay(segmentId);

			parade = this.paradeService.findBySegment(segment.getId());

			result.addObject("segment", segment);
			result.addObject("paradeId", parade.getId());

		} catch (final Exception e) {

			result = new ModelAndView("redirect:../error.do");
		}

		return result;
	}

}
