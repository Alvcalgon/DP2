
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
import services.PeriodRecordService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.PeriodRecord;

@Controller
@RequestMapping("/periodRecord/brotherhood")
public class PeriodRecordBrotherhoodController extends AbstractController {

	// Services -------------------------------------

	@Autowired
	private PeriodRecordService	periodRecordService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Constructors ---------------------------------------

	public PeriodRecordBrotherhoodController() {
		super();
	}

	//Creating----------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {

		ModelAndView result;
		PeriodRecord periodRecord;

		try {
			periodRecord = this.periodRecordService.create();
			result = this.createEditModelAndView(periodRecord);
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;

	}

	// Edition -------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int periodRecordId) {
		ModelAndView result;
		PeriodRecord periodRecord;
		Integer brotherhoodId;

		try {
			periodRecord = this.periodRecordService.findOneEdit(periodRecordId);
			Assert.notNull(periodRecord);
			result = new ModelAndView("periodRecord/edit");
			brotherhoodId = this.brotherhoodService.findByPrincipal().getId();

			result.addObject("periodRecord", periodRecord);
			result.addObject("brotherhoodId", brotherhoodId);
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

	//Saving-----------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final PeriodRecord periodRecord, final BindingResult binding) {

		ModelAndView result;
		Brotherhood brotherhood;

		try {
			brotherhood = this.brotherhoodService.findByPrincipal();

			if (binding.hasErrors())
				result = this.createEditModelAndView(periodRecord);
			else
				try {
					this.periodRecordService.save(periodRecord);
					result = new ModelAndView("redirect:/history/display.do?brotherhoodId=" + brotherhood.getId());
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(periodRecord, "periodRecord.commit.error");
				}
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final PeriodRecord periodRecord, final BindingResult binding) {
		ModelAndView result;
		Brotherhood brotherhood;

		try {
			brotherhood = this.brotherhoodService.findByPrincipal();

			try {
				this.periodRecordService.delete(periodRecord);
				result = new ModelAndView("redirect:../../history/display.do?brotherhoodId=" + brotherhood.getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(periodRecord, "periodRecord.commit.error");
			}
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

	// Ancillary -------------------------------------------------

	protected ModelAndView createEditModelAndView(final PeriodRecord periodRecord) {

		ModelAndView result;
		result = this.createEditModelAndView(periodRecord, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final PeriodRecord periodRecord, final String messageCode) {
		assert periodRecord != null;

		ModelAndView result;
		Integer brotherhoodId;
		brotherhoodId = this.brotherhoodService.findByPrincipal().getId();

		result = new ModelAndView("periodRecord/edit");
		result.addObject("periodRecord", periodRecord);
		result.addObject("messageCode", messageCode);
		result.addObject("brotherhoodId", brotherhoodId);

		return result;

	}
}
