
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
import services.MiscellaneousRecordService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.MiscellaneousRecord;

@Controller
@RequestMapping("/miscellaneousRecord/brotherhood")
public class MiscellaneousRecordBrotherhoodController extends AbstractController {

	// Services -------------------------------------

	@Autowired
	private MiscellaneousRecordService	miscellaneousRecordService;

	@Autowired
	private BrotherhoodService			brotherhoodService;


	// Constructors ---------------------------------------

	public MiscellaneousRecordBrotherhoodController() {
		super();
	}

	//Creating----------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {

		ModelAndView result;
		MiscellaneousRecord miscellaneousRecord;

		try {
			miscellaneousRecord = this.miscellaneousRecordService.create();
			result = this.createEditModelAndView(miscellaneousRecord);
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;

	}

	// Edition -------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int miscellaneousRecordId) {
		ModelAndView result;
		MiscellaneousRecord miscellaneousRecord;
		Integer brotherhoodId;

		try {
			miscellaneousRecord = this.miscellaneousRecordService.findOneEdit(miscellaneousRecordId);
			Assert.notNull(miscellaneousRecord);
			result = new ModelAndView("miscellaneousRecord/edit");
			brotherhoodId = this.brotherhoodService.findByPrincipal().getId();

			result.addObject("miscellaneousRecord", miscellaneousRecord);
			result.addObject("brotherhoodId", brotherhoodId);
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

	//Saving-----------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final MiscellaneousRecord miscellaneousRecord, final BindingResult binding) {

		ModelAndView result;
		Brotherhood brotherhood;

		try {
			brotherhood = this.brotherhoodService.findByPrincipal();

			if (binding.hasErrors())
				result = this.createEditModelAndView(miscellaneousRecord);
			else
				try {
					this.miscellaneousRecordService.save(miscellaneousRecord);
					result = new ModelAndView("redirect:/history/display.do?brotherhoodId=" + brotherhood.getId());
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(miscellaneousRecord, "miscellaneousRecord.commit.error");
				}
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final MiscellaneousRecord miscellaneousRecord, final BindingResult binding) {
		ModelAndView result;
		Brotherhood brotherhood;

		try {
			brotherhood = this.brotherhoodService.findByPrincipal();

			try {
				this.miscellaneousRecordService.delete(miscellaneousRecord);
				result = new ModelAndView("redirect:../../history/display.do?brotherhoodId=" + brotherhood.getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(miscellaneousRecord, "miscellaneousRecord.commit.error");
			}
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

	// Ancillary -------------------------------------------------

	protected ModelAndView createEditModelAndView(final MiscellaneousRecord miscellaneousRecord) {

		ModelAndView result;
		result = this.createEditModelAndView(miscellaneousRecord, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final MiscellaneousRecord miscellaneousRecord, final String messageCode) {
		assert miscellaneousRecord != null;

		ModelAndView result;
		Integer brotherhoodId;
		brotherhoodId = this.brotherhoodService.findByPrincipal().getId();

		result = new ModelAndView("miscellaneousRecord/edit");
		result.addObject("miscellaneousRecord", miscellaneousRecord);
		result.addObject("messageCode", messageCode);
		result.addObject("brotherhoodId", brotherhoodId);

		return result;

	}
}
