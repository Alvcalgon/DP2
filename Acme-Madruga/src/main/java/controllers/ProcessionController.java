
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

	//	@RequestMapping(value = "/display", method = RequestMethod.GET)
	//	public ModelAndView display(@RequestParam final int floatId) {
	//		ModelAndView result;
	//		Float floatt;
	//		Collection<String> pictures;
	//
	//		floatt = this.floatService.findOne(floatId);
	//		pictures = this.utilityService.getSplittedString(floatt.getPictures());
	//
	//		result = new ModelAndView("float/display");
	//		result.addObject("floatt", floatt);
	//		result.addObject("pictures", pictures);
	//
	//		return result;
	//	}

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
