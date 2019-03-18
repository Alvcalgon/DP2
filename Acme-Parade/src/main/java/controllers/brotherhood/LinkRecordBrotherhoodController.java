
package controllers.brotherhood;

import java.util.Collection;

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
import services.LinkRecordService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.LinkRecord;

@Controller
@RequestMapping("/linkRecord/brotherhood")
public class LinkRecordBrotherhoodController extends AbstractController {

	// Services -------------------------------------

	@Autowired
	private LinkRecordService	linkRecordService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Constructors ---------------------------------------

	public LinkRecordBrotherhoodController() {
		super();
	}

	//Creating----------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {

		ModelAndView result;
		LinkRecord linkRecord;

		try {
			linkRecord = this.linkRecordService.create();
			result = this.createEditModelAndView(linkRecord);
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;

	}

	// Edition -------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int linkRecordId) {
		ModelAndView result;
		LinkRecord linkRecord;
		Integer brotherhoodId;
		Collection<Brotherhood> brotherhoods;

		try {
			linkRecord = this.linkRecordService.findOneEdit(linkRecordId);
			Assert.notNull(linkRecord);
			result = new ModelAndView("linkRecord/edit");
			brotherhoodId = this.brotherhoodService.findByPrincipal().getId();
			brotherhoods = this.brotherhoodService.findAll();
			brotherhoods.remove(this.brotherhoodService.findByPrincipal());

			result.addObject("brotherhoods", brotherhoods);
			result.addObject("linkRecord", linkRecord);
			result.addObject("brotherhoodId", brotherhoodId);
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

	//Saving-----------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final LinkRecord linkRecord, final BindingResult binding) {

		ModelAndView result;
		Brotherhood brotherhood;

		try {
			brotherhood = this.brotherhoodService.findByPrincipal();

			if (binding.hasErrors())
				result = this.createEditModelAndView(linkRecord);
			else
				try {
					this.linkRecordService.save(linkRecord);
					result = new ModelAndView("redirect:/history/display.do?brotherhoodId=" + brotherhood.getId());
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(linkRecord, "linkRecord.commit.error");
				}
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final LinkRecord linkRecord, final BindingResult binding) {
		ModelAndView result;
		Brotherhood brotherhood;

		try {
			brotherhood = this.brotherhoodService.findByPrincipal();

			try {
				this.linkRecordService.delete(linkRecord);
				result = new ModelAndView("redirect:../../history/display.do?brotherhoodId=" + brotherhood.getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(linkRecord, "linkRecord.commit.error");
			}
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

	// Ancillary -------------------------------------------------

	protected ModelAndView createEditModelAndView(final LinkRecord linkRecord) {

		ModelAndView result;
		result = this.createEditModelAndView(linkRecord, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final LinkRecord linkRecord, final String messageCode) {
		assert linkRecord != null;

		ModelAndView result;
		Integer brotherhoodId;
		Collection<Brotherhood> brotherhoods;

		brotherhoodId = this.brotherhoodService.findByPrincipal().getId();
		brotherhoods = this.brotherhoodService.findAll();
		brotherhoods.remove(this.brotherhoodService.findByPrincipal());

		result = new ModelAndView("linkRecord/edit");
		result.addObject("linkRecord", linkRecord);
		result.addObject("messageCode", messageCode);
		result.addObject("brotherhoodId", brotherhoodId);
		result.addObject("brotherhoods", brotherhoods);

		return result;

	}
}
