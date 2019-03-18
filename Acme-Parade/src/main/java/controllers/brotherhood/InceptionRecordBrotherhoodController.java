
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
import services.HistoryService;
import services.InceptionRecordService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.InceptionRecord;

@Controller
@RequestMapping("/inceptionRecord/brotherhood")
public class InceptionRecordBrotherhoodController extends AbstractController {

	// Services -------------------------------------

	@Autowired
	private InceptionRecordService	inceptionRecordService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private HistoryService			historyService;


	// Constructors ---------------------------------------

	public InceptionRecordBrotherhoodController() {
		super();
	}

	//Creating----------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {

		ModelAndView result;
		InceptionRecord inceptionRecord;

		try {
			inceptionRecord = this.inceptionRecordService.create();
			result = this.createEditModelAndView(inceptionRecord);
			result.addObject("existHistory", false);
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;

	}

	// Edition -------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int inceptionRecordId) {
		ModelAndView result;
		InceptionRecord inceptionRecord;
		Brotherhood brotherhood;

		try {
			inceptionRecord = this.inceptionRecordService.findOneEdit(inceptionRecordId);
			brotherhood = this.brotherhoodService.findByPrincipal();

			result = this.createEditModelAndView(inceptionRecord);
			result.addObject("brotherhoodId", brotherhood.getId());
			result.addObject("existHistory", true);
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

	//Saving-----------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final InceptionRecord inceptionRecord, final BindingResult binding) {

		ModelAndView result;
		Brotherhood brotherhood;

		try {
			brotherhood = this.brotherhoodService.findByPrincipal();

			if (binding.hasErrors())
				result = this.createEditModelAndView(inceptionRecord);
			else
				try {
					this.inceptionRecordService.save(inceptionRecord);
					result = new ModelAndView("redirect:/history/display.do?brotherhoodId=" + brotherhood.getId());
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(inceptionRecord, "inceptionRecord.commit.error");
				}
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;

	}
	// Ancillary -------------------------------------------------

	protected ModelAndView createEditModelAndView(final InceptionRecord inceptionRecord) {

		ModelAndView result;
		result = this.createEditModelAndView(inceptionRecord, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final InceptionRecord inceptionRecord, final String messageCode) {
		assert inceptionRecord != null;

		ModelAndView result;
		Integer brotherhoodId;
		brotherhoodId = this.brotherhoodService.findByPrincipal().getId();

		result = new ModelAndView("inceptionRecord/edit");
		result.addObject("inceptionRecord", inceptionRecord);
		result.addObject("messageCode", messageCode);
		result.addObject("brotherhoodId", brotherhoodId);

		return result;

	}
}
