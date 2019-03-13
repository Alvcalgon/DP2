
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
import domain.History;
import domain.InceptionRecord;

@Controller
@RequestMapping("/inceptionRecord/handyWorker")
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

		inceptionRecord = this.inceptionRecordService.create();
		result = this.createEditModelAndView(inceptionRecord);
		result.addObject("existHistory", false);

		return result;

	}

	// Edition -------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int inceptionRecordId) {
		ModelAndView result;
		InceptionRecord inceptionRecord;
		Brotherhood brotherhood;

		inceptionRecord = this.inceptionRecordService.findOneEdit(inceptionRecordId);
		brotherhood = this.brotherhoodService.findByPrincipal();

		result = this.createEditModelAndView(inceptionRecord);
		result.addObject("brotherhoodId", brotherhood.getId());
		result.addObject("existHistory", true);

		return result;
	}

	//Saving-----------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final InceptionRecord inceptionRecord, final BindingResult binding) {

		ModelAndView result;
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByPrincipal();

		if (binding.hasErrors())
			result = this.createEditModelAndView(inceptionRecord);
		else
			try {
				History history;
				history = this.historyService.findHistoryByBrotherhood(brotherhood.getId());
				if (history == null) {
					history = this.historyService.create();
					this.historyService.addInceptionRecord(history, inceptionRecord);
					this.historyService.save(history);
				} else
					this.inceptionRecordService.save(inceptionRecord);
				result = new ModelAndView("redirect:/history/display.do?brotherhoodId=" + brotherhood.getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(inceptionRecord, "inceptionRecord.commit.error");
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
