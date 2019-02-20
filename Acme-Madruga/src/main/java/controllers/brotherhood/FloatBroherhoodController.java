
package controllers.brotherhood;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.FloatService;
import controllers.AbstractController;
import domain.Float;

@Controller
@RequestMapping(value = "/float/brotherhood")
public class FloatBroherhoodController extends AbstractController {

	@Autowired
	private FloatService	floatService;


	// Constructors -----------------------------------------------------------

	public FloatBroherhoodController() {
		super();
	}

	// Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Float paradeFloat;

		paradeFloat = this.floatService.create();

		result = this.createEditModelAndView(paradeFloat);

		return result;
	}

	// Edit

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int floatId) {
		ModelAndView result;
		Float paradeFloat;

		paradeFloat = this.floatService.findOneToEdit(floatId);
		Assert.notNull(paradeFloat);
		result = this.createEditModelAndView(paradeFloat);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Float paradeFloat, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(paradeFloat);
		else
			try {
				this.floatService.save(paradeFloat);
				result = new ModelAndView("redirect:../list.do?brotherhoodId=" + paradeFloat.getBrotherhood().getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(paradeFloat, "float.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Float paradeFloat, final BindingResult binding) {
		ModelAndView result;

		try {
			this.floatService.delete(paradeFloat);
			//TODO: cambiar url
			result = new ModelAndView("redirect:../../index/welcome.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(paradeFloat, "float.commit.error");
		}

		return result;
	}

	// Arcillary methods --------------------------

	protected ModelAndView createEditModelAndView(final Float paradeFloat) {
		ModelAndView result;

		result = this.createEditModelAndView(paradeFloat, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Float paradeFloat, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("float/edit");
		result.addObject("paradeFloat", paradeFloat);
		result.addObject("messageCode", messageCode);

		return result;

	}
}
