
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
import services.LegalRecordService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.LegalRecord;

@Controller
@RequestMapping("/legalRecord/brotherhood")
public class LegalRecordBrotherhoodController extends AbstractController {

	// Services -------------------------------------

	@Autowired
	private LegalRecordService	legalRecordService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Constructors ---------------------------------------

	public LegalRecordBrotherhoodController() {
		super();
	}

	//Creating----------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {

		ModelAndView result;
		LegalRecord legalRecord;

		try {
			legalRecord = this.legalRecordService.create();
			result = this.createEditModelAndView(legalRecord);
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;

	}

	// Edition -------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int legalRecordId) {
		ModelAndView result;
		LegalRecord legalRecord;
		Integer brotherhoodId;
		try {
			legalRecord = this.legalRecordService.findOneEdit(legalRecordId);
			Assert.notNull(legalRecord);
			result = new ModelAndView("legalRecord/edit");
			brotherhoodId = this.brotherhoodService.findByPrincipal().getId();

			result.addObject("legalRecord", legalRecord);
			result.addObject("brotherhoodId", brotherhoodId);
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

	//Saving-----------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final LegalRecord legalRecord, final BindingResult binding) {

		ModelAndView result;
		Brotherhood brotherhood;

		try {
			brotherhood = this.brotherhoodService.findByPrincipal();

			if (binding.hasErrors())
				result = this.createEditModelAndView(legalRecord);
			else
				try {
					this.legalRecordService.save(legalRecord);
					result = new ModelAndView("redirect:/history/display.do?brotherhoodId=" + brotherhood.getId());
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(legalRecord, "legalRecord.commit.error");
				}
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final LegalRecord legalRecord, final BindingResult binding) {
		ModelAndView result;
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByPrincipal();

		try {
			try {
				this.legalRecordService.delete(legalRecord);
				result = new ModelAndView("redirect:../../history/display.do?brotherhoodId=" + brotherhood.getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(legalRecord, "legalRecord.commit.error");
			}
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

	// Ancillary -------------------------------------------------

	protected ModelAndView createEditModelAndView(final LegalRecord legalRecord) {

		ModelAndView result;
		result = this.createEditModelAndView(legalRecord, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final LegalRecord legalRecord, final String messageCode) {
		assert legalRecord != null;

		ModelAndView result;
		Integer brotherhoodId;
		brotherhoodId = this.brotherhoodService.findByPrincipal().getId();

		result = new ModelAndView("legalRecord/edit");
		result.addObject("legalRecord", legalRecord);
		result.addObject("messageCode", messageCode);
		result.addObject("brotherhoodId", brotherhoodId);

		return result;

	}
}
