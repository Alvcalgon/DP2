
package controllers.administrator;

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

import services.AdministratorService;
import services.AreaService;
import controllers.AbstractController;
import domain.Administrator;
import domain.Area;

@Controller
@RequestMapping("/area/administrator")
public class AreaAdministratorController extends AbstractController {

	@Autowired
	private AreaService				areaService;

	@Autowired
	private AdministratorService	administratorService;


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

	// Create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Area area;

		try {
			area = this.areaService.create();

			result = this.createEditModelAndView(area);
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

	// Edit

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int areaId) {
		ModelAndView result;
		Area area;

		try {
			area = this.areaService.findOneToEditAdministrator(areaId);
			Assert.notNull(area);
			result = this.createEditModelAndView(area);
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Area area, final BindingResult binding) {
		ModelAndView result;
		Administrator admin;

		try {
			admin = this.administratorService.findByPrincipal();

			if (binding.hasErrors())
				result = this.createEditModelAndView(area);
			else
				try {
					this.areaService.save(area);
					result = new ModelAndView("redirect:../administrator/list.do");
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(area, "area.commit.error");
				}
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int areaId) {
		ModelAndView result;
		Administrator admin;
		Area area;

		try {
			admin = this.administratorService.findByPrincipal();
			area = this.areaService.findOneToEditAdministrator(areaId);

			try {
				this.areaService.delete(area);
				result = new ModelAndView("redirect:../administrator/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(area, "socialProfile.commit.error");
			}
		} catch (final Exception e) {
			result = new ModelAndView("redirect:../../error.do");
		}

		return result;
	}

	// Arcillary methods --------------------------

	protected ModelAndView createEditModelAndView(final Area area) {
		ModelAndView result;

		result = this.createEditModelAndView(area, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Area area, final String messageCode) {
		ModelAndView result;
		Administrator admin;
		int actorId;

		admin = this.administratorService.findByPrincipal();
		actorId = admin.getId();

		result = new ModelAndView("area/edit");
		result.addObject("area", area);
		result.addObject("messageCode", messageCode);
		result.addObject("actorId", actorId);

		return result;

	}

}
