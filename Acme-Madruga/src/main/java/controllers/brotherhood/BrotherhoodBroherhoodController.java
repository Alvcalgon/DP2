
package controllers.brotherhood;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AreaService;
import services.BrotherhoodService;
import controllers.AbstractController;
import domain.Area;
import domain.Brotherhood;
import forms.BrotherhoodForm;

@Controller
@RequestMapping(value = "/brotherhood/brotherhood")
public class BrotherhoodBroherhoodController extends AbstractController {

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private AreaService			areaService;


	// Constructors -----------------------------------------------------------

	public BrotherhoodBroherhoodController() {
		super();
	}

	// Edit

	@RequestMapping(value = "/selectArea", method = RequestMethod.GET)
	public ModelAndView selectArea() {
		ModelAndView result;
		final BrotherhoodForm brotherhoodForm;
		final Brotherhood brotherhood;

		try {
			brotherhood = this.brotherhoodService.findBrotherhoodToSelectArea();

			brotherhoodForm = new BrotherhoodForm();

			result = this.createEditModelAndView(brotherhoodForm);

		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

	@RequestMapping(value = "/selectArea", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final BrotherhoodForm brotherhoodForm, final BindingResult binding) {
		ModelAndView result;
		Brotherhood brotherhood;

		if (binding.hasErrors())
			result = this.createEditModelAndView(brotherhoodForm);
		else
			try {
				brotherhood = this.brotherhoodService.reconstruct(brotherhoodForm, binding);
				this.brotherhoodService.save(brotherhood);
				result = new ModelAndView("redirect:../list.do?brotherhoodId=" + brotherhood.getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(brotherhoodForm, "brotherhood.commit.error");
			}

		return result;
	}

	// Arcillary methods --------------------------

	protected ModelAndView createEditModelAndView(final BrotherhoodForm brotherhoodForm) {
		ModelAndView result;

		result = this.createEditModelAndView(brotherhoodForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final BrotherhoodForm brotherhoodForm, final String messageCode) {
		ModelAndView result;
		Collection<Area> areas;

		areas = this.areaService.findAll();
		result = new ModelAndView("brotherhood/selectArea");
		result.addObject("messageCode", messageCode);
		result.addObject("areas", areas);

		return result;

	}

}
