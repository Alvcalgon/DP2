
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.BrotherhoodService;
import services.ProcessionService;
import domain.Brotherhood;
import domain.Procession;

@Controller
@RequestMapping(value = "/procession")
public class ProcessionController extends AbstractController {

	@Autowired
	private ProcessionService	processionService;

	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Constructors -----------------------------------------------------------

	public ProcessionController() {
		super();
	}

	// Procession display ---------------------------------------------------------------		

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int processionId) {
		ModelAndView result;
		Procession procession;
		Brotherhood brotherhood;
		Brotherhood principal;
		Collection<domain.Float> floats;

		result = new ModelAndView("welcome/error");

		try {
			procession = this.processionService.findOneToDisplay(processionId);
			brotherhood = this.brotherhoodService.findBrotherhoodByProcession(processionId);
			floats = procession.getFloats();

			result = new ModelAndView("procession/display");
			result.addObject("procession", procession);
			result.addObject("brotherhood", brotherhood);
			result.addObject("floats", floats);

			if (LoginService.getPrincipal().getAuthorities().toString().equals("[BROTHERHOOD]"))
				if (brotherhood.getId() == this.brotherhoodService.findByPrincipal().getId()) {

					procession = this.processionService.findOne(processionId);
					principal = this.brotherhoodService.findByPrincipal();

					result.addObject("isOwner", true);
					result.addObject("procession", procession);
					result.addObject("principal", principal);
				}
		} catch (final Exception e) {
			//		result = new ModelAndView("redirect:../error.do");
		}

		return result;
	}

	// Procession list ---------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int brotherhoodId) {
		ModelAndView result;
		Collection<Procession> processions;
		Brotherhood principal;

		result = new ModelAndView("procession/list");
		processions = this.processionService.findProcessionFinalByBrotherhood(brotherhoodId);
		result.addObject("processions", processions);

		try {
			if (LoginService.getPrincipal().getAuthorities().toString().equals("[BROTHERHOOD]"))
				if (brotherhoodId == this.brotherhoodService.findByPrincipal().getId()) {

					principal = this.brotherhoodService.findByPrincipal();
					processions = this.processionService.findProcessionByBrotherhood(principal.getId());

					result.addObject("principal", principal);
					result.addObject("isOwner", true);
					result.addObject("processions", processions);
				}
		} catch (final Exception e) {
		}

		result.addObject("requestURI", "procession/list.do?brotherhoodId=" + brotherhoodId);
		return result;

	}
}
