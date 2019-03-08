
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.BrotherhoodService;
import services.FloatService;
import services.UtilityService;
import domain.Brotherhood;
import domain.Float;

@Controller
@RequestMapping(value = "/float")
public class FloatController extends AbstractController {

	@Autowired
	private FloatService		floatService;

	@Autowired
	private UtilityService		utilityService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Constructors -----------------------------------------------------------

	public FloatController() {
		super();
	}

	// Float display ---------------------------------------------------------------		

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int floatId) {
		ModelAndView result;
		Float floatt;
		Collection<String> pictures;

		result = new ModelAndView("float/display");

		try {
			floatt = this.floatService.findOne(floatId);
			pictures = this.utilityService.getSplittedString(floatt.getPictures());

			result = new ModelAndView("float/display");
			result.addObject("floatt", floatt);
			result.addObject("pictures", pictures);
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../error.do");
		}

		return result;
	}

	// Float list ---------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int brotherhoodId) {
		final ModelAndView result;
		final Collection<Float> floats;
		Brotherhood principal;

		floats = this.floatService.findFloatByBrotherhood(brotherhoodId);

		result = new ModelAndView("float/list");
		result.addObject("floats", floats);
		result.addObject("requestURI", "float/list.do?brotherhoodId=" + brotherhoodId);
		result.addObject("brotherhoodId", brotherhoodId);

		try {
			if (LoginService.getPrincipal().getAuthorities().toString().equals("[BROTHERHOOD]")) {
				principal = this.brotherhoodService.findByPrincipal();
				result.addObject("principal", principal);

			}
		} catch (final Exception e) {
		}
		return result;

	}
}
