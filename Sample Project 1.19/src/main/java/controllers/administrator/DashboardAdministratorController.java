
package controllers.administrator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

		// LEVEL C -----------------------------------------

		// Req 12.3.1
		Double[] dataMembersPerBrotherhood;
		dataMembersPerBrotherhood = this.memberService.findDataNumberMembersPerBrotherhood();

		//Req 12.3.2
		Collection<Brotherhood> largestBrotherhoods;
		largestBrotherhoods = this.brotherhoodService.findLargest();

		// Req 12.3.3
		Collection<Brotherhood> smallestBrotherhoods;
		smallestBrotherhoods = this.brotherhoodService.findSmallest();

		// Req 12.3.4
		Map<String, List<Double>> ratioRequestByProcession;
		ratioRequestByProcession = this.requestService.findRatioRequestByProcession();

		// Req 12.3.5
		Collection<Procession> processions;
		processions = this.processionService.findProcessionLess30days();

		// Req 12.3.6
		Double pendingRatio, approvedRatio, rejectedRatio;
		pendingRatio = this.requestService.findRatioPendingRequests();
		approvedRatio = this.requestService.findRatioAprovedRequests();
		rejectedRatio = this.requestService.findRatioRejectedRequets();

		// Req 12.3.7
		Collection<Member> members;
		members = this.memberService.memberRequestsAcceptedleast10();

		// Req 12.3.8
		ArrayList<String> histogramLabels;
		ArrayList<Integer> histogramValues;
		histogramValues = new ArrayList<Integer>(this.positionService.findHistogramValues());
		histogramLabels = new ArrayList<String>(this.positionService.findHistogramLabels(locale.getLanguage()));

		// LEVEL B --------------------------------------

		// Req 22.2.1
		final Double[] dataBrotherhoodPerArea;
		dataBrotherhoodPerArea = this.brotherhoodService.findDataNumberBrotherhoodPerArea();

		// Req 22.2.2
		Double[] dataResultsPerFinder;
		dataResultsPerFinder = this.processionService.findDataNumberResultsPerFinder();

		// Req 22.2.3
		Double ratioEmptyVsNonEmpty;
		ratioEmptyVsNonEmpty = this.finderService.findRatioEmptyVsNonEmpty();

		result = new ModelAndView("dashboard/display");

		// LEVEL C
		result.addObject("dataMembersPerBrotherhood", dataMembersPerBrotherhood);
		result.addObject("largestBrotherhoods", largestBrotherhoods);
		result.addObject("smallestBrotherhoods", smallestBrotherhoods);
		result.addObject("processions", processions);
		result.addObject("histogramValues", histogramValues);
		result.addObject("histogramLabels", histogramLabels);
		result.addObject("pendingRatio", pendingRatio);
		result.addObject("approvedRatio", approvedRatio);
		result.addObject("rejectedRatio", rejectedRatio);
		result.addObject("members", members);
		result.addObject("mapa", ratioRequestByProcession);

		// LEVEL B
		result.addObject("dataResultsPerFinder", dataResultsPerFinder);
		result.addObject("ratioEmptyVsNonEmpty", ratioEmptyVsNonEmpty);
		result.addObject("dataBrotherhoodPerArea", dataBrotherhoodPerArea);

		return result;
	}

}
