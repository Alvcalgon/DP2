
package controllers.chapter;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AreaService;
import services.ChapterService;
import controllers.AbstractController;
import domain.Area;
import domain.Chapter;

@Controller
@RequestMapping("/area/chapter")
public class AreaChapterController extends AbstractController {

	@Autowired
	private AreaService		areaService;

	@Autowired
	private ChapterService	chapterService;


	public AreaChapterController() {
		super();
	}

	@RequestMapping(value = "/listNotAssigned", method = RequestMethod.GET)
	public ModelAndView listAreasNotAssigned() {
		ModelAndView result;
		Collection<Area> areas;
		Chapter principal;
		boolean hasAssignedArea;

		principal = this.chapterService.findByPrincipal();
		hasAssignedArea = principal.getArea() == null;

		areas = this.areaService.findAreasNotAssigned();

		result = new ModelAndView("area/list");
		result.addObject("areas", areas);
		result.addObject("requestURI", "area/chapter/listNotAssigned.do");
		result.addObject("hasAssignedArea", hasAssignedArea);

		return result;
	}

	@RequestMapping(value = "/selfAssign")
	public ModelAndView selfAssign(@RequestParam final int areaId) {
		ModelAndView result;
		Area area;

		try {
			area = this.areaService.findOne(areaId);

			this.chapterService.selfAssignedArea(area);

			result = new ModelAndView("redirect:/welcome/index.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/error.do");
		}

		return result;
	}

}
