
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.FloatService;
import domain.Float;

@Controller
@RequestMapping(value = "/float")
public class FloatController extends AbstractController {

	@Autowired
	private FloatService	floatService;


	// Constructors -----------------------------------------------------------

	public FloatController() {
		super();
	}

	// Float display ---------------------------------------------------------------		

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int floatId) {
		ModelAndView result;
		Float paradeFloat;

		paradeFloat = this.floatService.findOne(floatId);

		result = new ModelAndView("float/display");
		result.addObject("paradeFloat", paradeFloat);

		return result;
	}

	// Float list ---------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int brotherhoodId) {
		final ModelAndView result;
		final Collection<Float> floats;

		floats = this.floatService.findFloatByBrotherhood(brotherhoodId);

		result = new ModelAndView("float/list");
		result.addObject("floats", floats);
		result.addObject("requestURI", "float/list.do?brotherhoodId=" + brotherhoodId);

		return result;

	}
}
