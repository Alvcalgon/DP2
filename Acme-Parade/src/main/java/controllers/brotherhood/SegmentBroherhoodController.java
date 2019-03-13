
package controllers.brotherhood;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ParadeService;
import services.SegmentService;
import controllers.AbstractController;
import domain.Parade;
import domain.Segment;
import forms.SegmentForm;

@Controller
@RequestMapping(value = "/segment/brotherhood")
public class SegmentBroherhoodController extends AbstractController {

	@Autowired
	private SegmentService	segmentService;

	@Autowired
	private ParadeService	paradeService;


	// Constructors -----------------------------------------------------------

	public SegmentBroherhoodController() {
		super();
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int paradeId) {
		ModelAndView result;
		final Segment segment;

		segment = new Segment();

		result = this.createEditModelAndView(segment, paradeId);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int segmentId) {
		ModelAndView result;
		final Segment segment;
		Parade parade;

		try {
			segment = this.segmentService.findOneToEdit(segmentId);
			parade = this.paradeService.findBySegment(segment.getId());
			result = this.createEditModelAndView(segment, parade.getId());
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final SegmentForm segmentForm, final BindingResult binding) {
		ModelAndView result;
		final Segment segment;

		segment = this.segmentService.reconstruct(segmentForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(segmentForm);
		else
			try {
				this.segmentService.save(segment);
				result = new ModelAndView("redirect:/parade/display.do?paradeId=" + segmentForm.getParadeId());
			} catch (final Exception e) {
				result = new ModelAndView("redirect:../../error.do");
			}
		return result;
	}

	// Arcillary methods --------------------------

	protected ModelAndView createEditModelAndView(final Segment segment, final int paradeId) {
		ModelAndView result;
		SegmentForm segmentForm;

		segmentForm = this.segmentService.createForm(segment, paradeId);

		result = this.createEditModelAndView(segmentForm, null);

		return result;
	}
	protected ModelAndView createEditModelAndView(final SegmentForm segmentForm) {
		ModelAndView result;

		result = this.createEditModelAndView(segmentForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final SegmentForm segmentForm, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("segment/edit");
		result.addObject("messageCode", messageCode);
		result.addObject("segmentForm", segmentForm);
		result.addObject("paradeId", segmentForm.getParadeId());

		return result;

	}

}
