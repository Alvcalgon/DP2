
package controllers.administrator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.BrotherhoodService;
import services.FinderService;
import services.MemberService;
import services.PositionService;
import services.ProcessionService;
import services.RequestService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Member;
import domain.Procession;

@Controller
@RequestMapping("dashboard/administrator")
public class DashboardAdministratorController extends AbstractController {

	// Services ------------------
	@Autowired
	private MemberService		memberService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private RequestService		requestService;

	@Autowired
	private ProcessionService	processionService;

	@Autowired
	private PositionService		positionService;

	@Autowired
	private FinderService		finderService;


	// Constructors --------------
	public DashboardAdministratorController() {
		super();
	}

	// methods --------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(final Locale locale) {
		ModelAndView result;
		ArrayList<String> histogramLabels;
		ArrayList<Integer> histogramValues;
		final Double ratioRequest;
		Double ratioEmptyVsNonEmpty;
		Double[] dataMembersPerBrotherhood, dataResultsPerFinder;
		Collection<Brotherhood> largestBrotherhoods, smallestBrotherhoods;
		Collection<Procession> processions;
		final Collection<Member> members;

		// LEVEL C -----------------------------------------
		dataMembersPerBrotherhood = this.memberService.findDataNumberMembersPerBrotherhood();
		largestBrotherhoods = this.brotherhoodService.findLargest();
		smallestBrotherhoods = this.brotherhoodService.findSmallest();
		//TODO: queries de Julia
		processions = this.processionService.findProcessionLess30days();
		histogramValues = new ArrayList<Integer>(this.positionService.findHistogramValues());
		histogramLabels = new ArrayList<String>(this.positionService.findHistogramLabels(locale.getLanguage()));

		// LEVEL B --------------------------------------
		dataResultsPerFinder = this.processionService.findDataNumberResultsPerFinder();
		ratioEmptyVsNonEmpty = this.finderService.findRatioEmptyVsNonEmpty();

		result = new ModelAndView("dashboard/display");

		// LEVEL C
		result.addObject("dataMembersPerBrotherhood", dataMembersPerBrotherhood);
		result.addObject("largestBrotherhoods", largestBrotherhoods);
		result.addObject("smallestBrotherhoods", smallestBrotherhoods);
		result.addObject("processions", processions);
		result.addObject("histogramValues", histogramValues);
		result.addObject("histogramLabels", histogramLabels);

		// LEVEL B
		result.addObject("dataResultsPerFinder", dataResultsPerFinder);
		result.addObject("ratioEmptyVsNonEmpty", ratioEmptyVsNonEmpty);

		return result;
	}

}
