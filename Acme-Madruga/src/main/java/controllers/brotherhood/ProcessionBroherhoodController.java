
package controllers.brotherhood;

import java.util.Collection;

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
import domain.Procession;

@Controller
@RequestMapping(value = "/procession/brotherhood")
public class ProcessionBroherhoodController extends AbstractController {

	@Autowired
	private ProcessionService	processionService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private FloatService		floatService;


	// Constructors -----------------------------------------------------------

	public ProcessionBroherhoodController() {
		super();
	}

	// Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Procession procession;

		procession = this.processionService.create();

		result = this.createEditModelAndView(procession);

		return result;
	}

	// Edit

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int processionId) {
		ModelAndView result;
		Procession procession;

		try {
			procession = this.processionService.findOneToEdit(processionId);

			result = this.createEditModelAndView(procession);
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Procession procession, final BindingResult binding) {
		ModelAndView result;
		Brotherhood brotherhood;

		if (binding.hasErrors())
			result = this.createEditModelAndView(procession);
		else
			try {
				this.processionService.save(procession);
				brotherhood = this.brotherhoodService.findBrotherhoodByProcession(procession.getId());
				result = new ModelAndView("redirect:../list.do?brotherhoodId=" + brotherhood.getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(procession, "procession.commit.error");
			}

		return result;
	}

	//	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	//	public ModelAndView delete(final FloatForm floatForm, final BindingResult binding) {
	//		ModelAndView result;
	//		Float floatt;
	//		int brotherhoodId;
	//
	//		floatt = this.floatService.findOneToEdit(floatForm.getId());
	//		brotherhoodId = floatt.getBrotherhood().getId();
	//
	//		try {
	//			this.floatService.delete(floatt);
	//			result = new ModelAndView("redirect:../../float/list.do?brotherhoodId=" + brotherhoodId);
	//		} catch (final Throwable oops) {
	//			result = this.createEditModelAndView(floatForm, "float.commit.error");
	//		}
	//
	//		return result;
	//	}

	// Arcillary methods --------------------------

	protected ModelAndView createEditModelAndView(final Procession procession) {
		ModelAndView result;

		result = this.createEditModelAndView(procession, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Procession procession, final String messageCode) {
		ModelAndView result;
		Brotherhood owner;
		Collection<Float> floats;

		owner = this.brotherhoodService.findByPrincipal();
		floats = this.floatService.findFloatByBrotherhood(owner.getId());

		result = new ModelAndView("procession/edit");
		result.addObject("procession", procession);
		result.addObject("messageCode", messageCode);
		result.addObject("owner", owner);
		result.addObject("floats", floats);

		return result;

	}

}
