
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AreaService;
import services.BrotherhoodService;
import services.UtilityService;
import domain.Area;
import domain.Brotherhood;

@Controller
@RequestMapping(value = "/area")
public class AreaController extends AbstractController {

	@Autowired
	private AreaService			areaService;

	@Autowired
	private UtilityService		utilityService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


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
		Collection<Brotherhood> brotherhoods;

		result = new ModelAndView("area/display");

		try {
			area = this.areaService.findOne(areaId);
			pictures = this.utilityService.getSplittedString(area.getPictures());

			brotherhoods = this.brotherhoodService.findBrotherhoodFromArea(area);

			result.addObject("area", area);
			result.addObject("pictures", pictures);
			result.addObject("brotherhoods", brotherhoods);
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../error.do");
		}

		return result;
	}
}
