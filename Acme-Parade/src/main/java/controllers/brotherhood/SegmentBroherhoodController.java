
package controllers.brotherhood;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.ParadeService;
import services.SegmentService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Parade;
import domain.Segment;

@Controller
@RequestMapping(value = "/segment/brotherhood")
public class SegmentBroherhoodController extends AbstractController {

	@Autowired
	private SegmentService		segmentService;

	@Autowired
	private ParadeService		paradeService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Constructors -----------------------------------------------------------

	public SegmentBroherhoodController() {
		super();
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int paradeId) {
		ModelAndView result;
		final Segment segment;
		Parade parade;

		try {
			parade = this.paradeService.findOne(paradeId);
			segment = this.segmentService.create();

			result = this.createEditModelAndView(segment, parade.getId());

		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int segmentId) {
		ModelAndView result;
		final Segment segment;

		try {
			segment = this.segmentService.findOneToEdit(segmentId);
			result = this.createEditModelAndView(segment);
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Segment segment, final BindingResult binding, final HttpServletRequest request) {
		ModelAndView result;
		final Brotherhood principal;
		Parade parade;
		Integer paradeId;
		String paramParadeId;

		paramParadeId = request.getParameter("paradeId");
		paradeId = paramParadeId.isEmpty() ? null : Integer.parseInt(paramParadeId);

		if (binding.hasErrors())
			result = this.createEditModelAndView(segment);
		else
			try {
				if (segment.getId() == 0)
					parade = this.paradeService.findOne(paradeId);
				else
					parade = this.paradeService.findBySegment(segment.getId());

				this.segmentService.save(segment, parade);
				principal = this.brotherhoodService.findByPrincipal();
				result = new ModelAndView("redirect:/parade/list.do?brotherhoodId=" + principal.getId());
			} catch (final Exception e) {
				result = new ModelAndView("redirect:../../error.do");
			}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Segment segment, final BindingResult binding) {
		ModelAndView result;
		Parade parade;

		try {
			parade = this.paradeService.findBySegment(segment.getId());
			this.segmentService.delete(segment);
			result = new ModelAndView("redirect:/parade/display.do?paradeId=" + parade.getId());
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(segment, "segment.delete.error");
		}

		return result;
	}

	// Arcillary methods --------------------------

	protected ModelAndView createEditModelAndView(final Segment segment) {
		ModelAndView result;

		result = this.createEditModelAndView(segment, null, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Segment segment, final String messageCode) {
		ModelAndView result;

		result = this.createEditModelAndView(segment, null, messageCode);

		return result;

	}

	protected ModelAndView createEditModelAndView(final Segment segment, final int paradeId) {
		ModelAndView result;

		result = this.createEditModelAndView(segment, paradeId, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Segment segment, final Integer paradeId, final String messageCode) {
		ModelAndView result;
		Brotherhood principal;

		principal = this.brotherhoodService.findByPrincipal();

		result = new ModelAndView("segment/edit");
		result.addObject("messageCode", messageCode);
		result.addObject("segment", segment);
		result.addObject("paradeId", paradeId);
		result.addObject("principalId", principal.getId());
		return result;

	}

}
