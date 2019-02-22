
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

import services.BrotherhoodService;
import services.FloatService;
import services.ProcessionService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Float;

@Controller
@RequestMapping(value = "/float/brotherhood")
public class FloatBroherhoodController extends AbstractController {

	@Autowired
	private FloatService		floatService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private ProcessionService	processionService;


	// Constructors -----------------------------------------------------------

	public FloatBroherhoodController() {
		super();
	}

	// Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Float floatt;

		floatt = this.floatService.create();

		result = this.createEditModelAndView(floatt);

		return result;
	}

	// Edit

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int floatId) {
		ModelAndView result;
		Float floatt;

		floatt = this.floatService.findOneToEdit(floatId);
		Assert.notNull(floatt);
		result = this.createEditModelAndView(floatt);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Float floatt, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(floatt);
		else
			try {
				this.floatService.save(floatt);
				result = new ModelAndView("redirect:../list.do?brotherhoodId=" + floatt.getBrotherhood().getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(floatt, "float.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Float floatt, final BindingResult binding) {
		ModelAndView result;
		Brotherhood owner;

		try {
			this.floatService.delete(floatt);
			owner = this.brotherhoodService.findByPrincipal();
			result = new ModelAndView("redirect:../../float/list.do?brotherhoodId=" + owner.getId());
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(floatt, "float.commit.error");
		}

		return result;
	}

	// Arcillary methods --------------------------

	protected ModelAndView createEditModelAndView(final Float floatt) {
		ModelAndView result;

		result = this.createEditModelAndView(floatt, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Float floatt, final String messageCode) {
		ModelAndView result;
		Brotherhood owner;
		Boolean notProcession;

		owner = this.brotherhoodService.findByPrincipal();
		notProcession = this.processionService.floatBelongtToProcession(floatt.getId());
		result = new ModelAndView("float/edit");
		result.addObject("floatt", floatt);
		result.addObject("messageCode", messageCode);
		result.addObject("owner", owner);
		result.addObject("notProcession", notProcession);

		return result;

	}
}
