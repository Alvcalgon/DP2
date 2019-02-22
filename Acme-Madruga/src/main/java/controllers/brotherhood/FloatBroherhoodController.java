
package controllers.brotherhood;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import forms.FloatForm;

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
		FloatForm floatForm;

		floatForm = new FloatForm();

		result = this.createEditModelAndView(floatForm);

		return result;
	}

	// Edit

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int floatId) {
		ModelAndView result;
		FloatForm floatForm;
		Brotherhood brotherhood;
		Float floatt;

		floatt = this.floatService.findOneToEdit(floatId);

		brotherhood = this.brotherhoodService.findByPrincipal();
		floatForm = new FloatForm();
		floatForm.setTitle(floatt.getTitle());
		floatForm.setId(floatt.getId());
		floatForm.setVersion(floatt.getVersion());
		floatForm.setPictures(floatt.getPictures());
		floatForm.setDescription(floatt.getDescription());
		floatForm.setBrotherhood(brotherhood);
		result = this.createEditModelAndView(floatForm);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final FloatForm floatForm, final BindingResult binding) {
		ModelAndView result;
		Float floatt;

		this.floatService.validateTitle(floatForm, binding);
		this.floatService.validateDescription(floatForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(floatForm);
		else
			try {
				floatt = this.floatService.reconstruct(floatForm, binding);
				this.floatService.save(floatt);
				result = new ModelAndView("redirect:../list.do?brotherhoodId=" + floatt.getBrotherhood().getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(floatForm, "float.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final FloatForm floatForm, final BindingResult binding) {
		ModelAndView result;
		Float floatt;
		int brotherhoodId;

		floatt = this.floatService.findOneToEdit(floatForm.getId());
		brotherhoodId = floatt.getBrotherhood().getId();

		try {
			this.floatService.delete(floatt);
			result = new ModelAndView("redirect:../../float/list.do?brotherhoodId=" + brotherhoodId);
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(floatForm, "float.commit.error");
		}

		return result;
	}

	// Arcillary methods --------------------------

	protected ModelAndView createEditModelAndView(final FloatForm floatForm) {
		ModelAndView result;

		result = this.createEditModelAndView(floatForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final FloatForm floatForm, final String messageCode) {
		ModelAndView result;
		Brotherhood owner;
		Boolean notProcession;

		notProcession = this.processionService.floatBelongtToProcession(floatForm.getId());
		owner = this.brotherhoodService.findByPrincipal();
		result = new ModelAndView("float/edit");
		result.addObject("floatForm", floatForm);
		result.addObject("messageCode", messageCode);
		result.addObject("owner", owner);
		result.addObject("notProcession", notProcession);

		return result;

	}

}
