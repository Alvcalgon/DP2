
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import services.BrotherhoodService;
import services.CustomisationService;
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
	private ProcessionService		processionService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private FloatService			floatService;

	@Autowired
	private CustomisationService	customisationService;


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
		Procession saved;
		int rowLimit;
		int columnLimit;

		if (binding.hasErrors())
			result = this.createEditModelAndView(procession);
		else
			try {
				rowLimit = this.customisationService.find().getRowLimit();
				columnLimit = this.customisationService.find().getColumnLimit();
				saved = this.processionService.save(procession, rowLimit, columnLimit);
				brotherhood = this.brotherhoodService.findBrotherhoodByProcession(saved.getId());
				result = new ModelAndView("redirect:../list.do?brotherhoodId=" + brotherhood.getId());
			} catch (final IllegalArgumentException invalidMoment) {
				if (invalidMoment.getMessage().equals("Invalid moment"))
					result = this.createEditModelAndView(procession, "procession.invalid.moment");
				else
					result = this.createEditModelAndView(procession, "procession.commit.error");
			}

			catch (final Throwable oops) {
				result = this.createEditModelAndView(procession, "procession.commit.error");
			}

		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Procession procession, final BindingResult binding) {
		ModelAndView result;
		int brotherhoodId;

		try {
			brotherhoodId = this.brotherhoodService.findBrotherhoodByProcession(procession.getId()).getId();
			this.processionService.delete(procession);
			result = new ModelAndView("redirect:../../procession/list.do?brotherhoodId=" + brotherhoodId);
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(procession, "procession.delete.error");
		}

		return result;
	}

	@RequestMapping(value = "/makeFinal", method = RequestMethod.GET)
	public ModelAndView makeFinal(@RequestParam final int processionId, final RedirectAttributes redir) {
		ModelAndView result;
		Procession procession;

		procession = this.processionService.findOne(processionId);

		try {
			this.processionService.makeFinal(procession);
		} catch (final Throwable oops) {
			redir.addFlashAttribute("messageCode", "procession.make.final.error");
		}

		result = new ModelAndView("redirect:/procession/display.do?processionId=" + processionId);

		return result;
	}

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
