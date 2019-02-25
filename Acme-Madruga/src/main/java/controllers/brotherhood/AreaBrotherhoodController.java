
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

import services.AreaService;
import controllers.AbstractController;
import domain.Area;

@Controller
@RequestMapping(value = "area/brotherhood/")
public class AreaBrotherhoodController extends AbstractController {

	// Services 

	@Autowired
	private AreaService	areaService;


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
	public ModelAndView edit(@Valid final Area area, final BindingResult binding, final HttpServletRequest request) {
		ModelAndView result;
		Integer brotherhoodId;
		String paramBrotherhodId;

		paramBrotherhodId = request.getParameter("brotherhoodId");
		brotherhoodId = paramBrotherhodId == null || paramBrotherhodId.isEmpty() ? null : Integer.parseInt(paramBrotherhodId);

		if (binding.hasErrors())
			result = this.createEditModelAndView(area, brotherhoodId);
		else
			try {
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

		result = new ModelAndView("area/edit");
		result.addObject("area", area);
		result.addObject("brotherhoodId", brotherhoodId);

		result.addObject("messageCode", messageCode);

		return result;
	}

}
