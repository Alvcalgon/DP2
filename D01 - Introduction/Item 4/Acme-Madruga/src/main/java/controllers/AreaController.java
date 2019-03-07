
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AreaService;
import services.UtilityService;
import domain.Area;

@Controller
@RequestMapping(value = "/area")
public class AreaController extends AbstractController {

	@Autowired
	private AreaService		areaService;

	@Autowired
	private UtilityService	utilityService;


	// Constructors -----------------------------------------------------------

	public AreaController() {
		super();
	}

	// Float display ---------------------------------------------------------------		

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int areaId) {
		ModelAndView result;
		Area area;
		Collection<String> pictures;

		result = new ModelAndView("area/display");

		try {
			area = this.areaService.findOne(areaId);
			pictures = this.utilityService.getSplittedString(area.getPictures());

			result.addObject("area", area);
			result.addObject("pictures", pictures);
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../error.do");
		}

		return result;
	}
}
