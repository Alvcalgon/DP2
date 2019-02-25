
package controllers.brotherhood;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import services.AreaService;
import services.BrotherhoodService;
import controllers.AbstractController;
import converters.StringToAreaConverter;
import domain.Area;
import domain.Brotherhood;

@Controller
@RequestMapping(value = "area/brotherhood/")
public class AreaBrotherhoodController extends AbstractController {

	// Services 

	@Autowired
	private AreaService				areaService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	// Converters

	@Autowired
	private StringToAreaConverter	stringToAreaConverter;


	// Constructor

	public AreaBrotherhoodController() {
		super();
	}

	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int brotherhoodId) {
		ModelAndView result;
		Area area;

		area = this.areaService.create();
		result = this.createEditModelAndView(area, brotherhoodId);

		return result;
	}

	// Edit
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(final HttpServletRequest request, final RedirectAttributes redir) {
		ModelAndView result;
		Integer brotherhoodId;
		String paramBrotherhodId;
		Area area;
		String areaId;

		paramBrotherhodId = request.getParameter("brotherhoodId");
		brotherhoodId = paramBrotherhodId == null || paramBrotherhodId.isEmpty() ? null : Integer.parseInt(paramBrotherhodId);

		areaId = request.getParameter("areaId");

		area = this.stringToAreaConverter.convert(areaId);

		try {
			this.areaService.findOneToEditBrotherhood(brotherhoodId);
			this.areaService.save(area, brotherhoodId);
			result = new ModelAndView("redirect:/welcome/index.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(area, brotherhoodId, "area.commit.error");
		}

		return result;

	}

	// Ancillary methods ------------------------------------------------------
	protected ModelAndView createEditModelAndView(final Area area, final Integer brotherhoodId) {
		ModelAndView result;

		result = this.createEditModelAndView(area, brotherhoodId, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Area area, final Integer brotherhoodId, final String messageCode) {
		ModelAndView result;
		Collection<Area> areas;
		Brotherhood brotherhood;

		try {
			brotherhood = this.brotherhoodService.findByPrincipal();
			areas = this.areaService.findAllByBrotherhood(brotherhood);

			result = new ModelAndView("area/edit");
			result.addObject("area", area);
			result.addObject("brotherhoodId", brotherhood.getId());
			result.addObject("areas", areas);

			result.addObject("messageCode", messageCode);
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

}
