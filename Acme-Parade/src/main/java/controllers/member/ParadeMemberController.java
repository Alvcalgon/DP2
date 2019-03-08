
package controllers.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.CustomisationService;
import services.FinderService;
import controllers.AbstractController;
import domain.Customisation;
import domain.Finder;

@Controller
@RequestMapping("parade/member/")
public class ParadeMemberController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private FinderService			finderService;

	@Autowired
	private CustomisationService	customisationService;


	// Constructors -----------------------------------------------------------

	public ParadeMemberController() {
		super();
	}

	// Controller methods -----------------------------------------------------		

	@RequestMapping(value = "/listFinder", method = RequestMethod.GET)
	public ModelAndView listFinder() {
		ModelAndView result;
		Customisation customisation;
		int numberOfResults;
		Finder finder;

		customisation = this.customisationService.find();
		numberOfResults = customisation.getMaxNumberResults();
		finder = this.finderService.findByMemberPrincipal();
		finder = this.finderService.evaluateSearch(finder);

		result = new ModelAndView("parade/listFinder");
		result.addObject("requestURI", "parade/member/listFinder.do");
		result.addObject("finder", finder);
		result.addObject("parades", finder.getParades());
		result.addObject("numberOfResults", numberOfResults);

		return result;
	}

}
