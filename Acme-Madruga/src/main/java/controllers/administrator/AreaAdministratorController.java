
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AreaService;
import controllers.AbstractController;
import domain.Area;

@Controller
@RequestMapping("/area/administrator")
public class AreaAdministratorController extends AbstractController {

	@Autowired
	private AreaService	areaService;


	public AreaAdministratorController() {
		super();
	}

	// Request List -----------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		Collection<Area> areas;

		areas = this.areaService.findAll();

		result = new ModelAndView("area/list");
		result.addObject("areas", areas);
		result.addObject("requestURI", "area/administrator/list.do");

		return result;
	}

	//	//Edit
	//	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	//	public ModelAndView edit(@RequestParam final int requestId) {
	//		ModelAndView result;
	//		Request request;
	//
	//		try {
	//			request = this.requestService.findOneToBrotherhood(requestId);
	//			Assert.notNull(request);
	//			result = this.createEditModelAndView(request);
	//		} catch (final Exception e) {
	//			result = new ModelAndView("redirect:../../error.do");
	//		}
	//
	//		return result;
	//	}
	//
	//	//Save
	//	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	//	public ModelAndView save(@Valid final Request request, final BindingResult binding) {
	//		ModelAndView result;
	//		Brotherhood brotherhood;
	//
	//		try {
	//			brotherhood = this.brotherhoodService.findByPrincipal();
	//
	//			if (binding.hasErrors())
	//				result = this.createEditModelAndView(request);
	//			else
	//				try {
	//					this.requestService.saveEdit(request);
	//					result = new ModelAndView("redirect:../../brotherhood,member/request/list.do");
	//				} catch (final Throwable oops) {
	//					result = this.createEditModelAndView(request, "request.commit.error");
	//				}
	//		} catch (final Exception e) {
	//			result = new ModelAndView("redirect:../../error.do");
	//		}
	//
	//		return result;
	//	}
	//
	//	// Arcillary methods --------------------------
	//
	//	protected ModelAndView createEditModelAndView(final Request request) {
	//		ModelAndView result;
	//
	//		result = this.createEditModelAndView(request, null);
	//
	//		return result;
	//	}
	//
	//	protected ModelAndView createEditModelAndView(final Request request, final String messageCode) {
	//		ModelAndView result;
	//		int brotherhoodId;
	//
	//		brotherhoodId = this.brotherhoodService.findByPrincipal().getId();
	//
	//		result = new ModelAndView("request/edit");
	//		result.addObject("request", request);
	//		result.addObject("messageCode", messageCode);
	//		result.addObject("brotherhoodId", brotherhoodId);
	//
	//		return result;
	//
	//	}

}
