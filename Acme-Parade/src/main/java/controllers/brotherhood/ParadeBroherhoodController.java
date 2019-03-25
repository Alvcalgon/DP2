
package controllers.brotherhood;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import services.BrotherhoodService;
import services.FloatService;
import services.ParadeService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Float;
import domain.Parade;

@Controller
@RequestMapping(value = "/parade/brotherhood")
public class ParadeBroherhoodController extends AbstractController {

	@Autowired
	private ParadeService		paradeService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private FloatService		floatService;


	// Constructors -----------------------------------------------------------

	public ParadeBroherhoodController() {
		super();
	}

	// Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Parade parade;

		parade = this.paradeService.create();

		result = this.createEditModelAndView(parade);

		return result;
	}

	// Edit

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int paradeId) {
		ModelAndView result;
		Parade parade;

		try {
			parade = this.paradeService.findOneToEdit(paradeId);

			result = this.createEditModelAndView(parade);
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Parade parade, final BindingResult binding) {
		ModelAndView result;
		Brotherhood brotherhood;
		Parade saved;
		Parade paradeRec;

		paradeRec = this.paradeService.reconstructSave(parade, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(parade);
		else
			try {

				saved = this.paradeService.save(paradeRec);
				brotherhood = this.brotherhoodService.findBrotherhoodByParade(saved.getId());
				result = new ModelAndView("redirect:../list.do?brotherhoodId=" + brotherhood.getId());
			} catch (final IllegalArgumentException invalidMoment) {
				if (invalidMoment.getMessage().equals("Invalid moment"))
					result = this.createEditModelAndView(parade, "parade.invalid.moment");
				else
					result = this.createEditModelAndView(parade, "parade.commit.error");
			}

			catch (final Throwable oops) {
				result = this.createEditModelAndView(parade, "parade.commit.error");
			}

		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Parade parade, final BindingResult binding) {
		ModelAndView result;
		int brotherhoodId;
		Parade paradeBbdd;

		try {
			paradeBbdd = this.paradeService.findOneToEdit(parade.getId());
			brotherhoodId = this.brotherhoodService.findBrotherhoodByParade(parade.getId()).getId();

			this.paradeService.delete(paradeBbdd);
			result = new ModelAndView("redirect:../../parade/list.do?brotherhoodId=" + brotherhoodId);
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(parade, "parade.delete.error");
		}

		return result;
	}

	@RequestMapping(value = "/makeFinal", method = RequestMethod.GET)
	public ModelAndView makeFinal(@RequestParam final int paradeId, final RedirectAttributes redir) {
		ModelAndView result;
		Parade parade;

		parade = this.paradeService.findOne(paradeId);

		try {
			this.paradeService.makeFinal(parade);
		} catch (final Throwable oops) {
			redir.addFlashAttribute("messageCode", "parade.make.final.error");
		}

		result = new ModelAndView("redirect:/parade/display.do?paradeId=" + paradeId);

		return result;
	}

	@RequestMapping(value = "/makeCopy", method = RequestMethod.GET)
	public ModelAndView makeCopy(@RequestParam final int paradeId, final RedirectAttributes redir) {
		ModelAndView result;
		Parade parade;
		Brotherhood principal;

		parade = this.paradeService.findOne(paradeId);

		principal = this.brotherhoodService.findByPrincipal();

		try {
			this.paradeService.makeCopy(parade);
		} catch (final Throwable oops) {
			redir.addFlashAttribute("messageCode", "parade.make.copy.error");

		}

		result = new ModelAndView("redirect:/parade/list.do?brotherhoodId=" + principal.getId());
		return result;
	}

	// Arcillary methods --------------------------

	protected ModelAndView createEditModelAndView(final Parade parade) {
		ModelAndView result;

		result = this.createEditModelAndView(parade, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Parade parade, final String messageCode) {
		ModelAndView result;
		Brotherhood owner;
		Collection<Float> floats;

		owner = this.brotherhoodService.findByPrincipal();
		floats = this.floatService.findFloatByBrotherhood(owner.getId());

		result = new ModelAndView("parade/edit");
		result.addObject("parade", parade);
		result.addObject("messageCode", messageCode);
		result.addObject("owner", owner);
		result.addObject("floats", floats);

		return result;

	}

}
