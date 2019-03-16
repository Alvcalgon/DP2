
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

import services.AreaService;
import services.BrotherhoodService;
import services.ChapterService;
import services.FinderService;
import services.HistoryService;
import services.MemberService;
import services.ParadeService;
import services.PositionService;
import services.RequestService;
import services.SponsorService;
import services.SponsorshipService;
import controllers.AbstractController;
import domain.Brotherhood;
import domain.Chapter;
import domain.Member;
import domain.Parade;
import domain.Sponsor;

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
	private ParadeService		paradeService;

	@Autowired
	private PositionService		positionService;

	@Autowired
	private FinderService		finderService;

	@Autowired
	private HistoryService		historyService;

	@Autowired
	private ChapterService		chapterService;

	@Autowired
	private AreaService			areaService;

	@Autowired
	private SponsorService		sponsorService;

	@Autowired
	private SponsorshipService	sponsorshipService;


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
		Map<String, List<Double>> ratioRequestByParade;
		ratioRequestByParade = this.requestService.findRatioRequestByParade();

		// Req 12.3.5
		Collection<Parade> parades;
		parades = this.paradeService.findParadeLess30days();

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

		//Req Acme-Parade 4.1
		Collection<Brotherhood> findBrotherhoohLargestHistory;
		Double[] findDataNumberRecordsPerHistory;
		Collection<Brotherhood> findBrotherhoohsLargestHistoryAvg;
		findBrotherhoohLargestHistory = this.historyService.findBrotherhoohLargestHistory();
		findDataNumberRecordsPerHistory = this.historyService.findDataNumberRecordsPerHistory();
		findBrotherhoohsLargestHistoryAvg = this.historyService.findBrotherhoohsLargestHistoryAvg();

		// LEVEL B --------------------------------------

		// Req 22.2.1
		final Double[] dataBrotherhoodPerArea;
		dataBrotherhoodPerArea = this.brotherhoodService.findDataNumberBrotherhoodPerArea();

		Map<String, Double> ratioBrotherhoodsPerArea;
		Map<String, Long> countBrotherhoodsPerArea;

		countBrotherhoodsPerArea = this.brotherhoodService.countBrotherhoodPerArea();
		ratioBrotherhoodsPerArea = this.brotherhoodService.ratioBrotherhoodPerArea();

		// Req 22.2.2
		Double[] dataResultsPerFinder;
		dataResultsPerFinder = this.paradeService.findDataNumberResultsPerFinder();

		// Req 22.2.3
		Double ratioEmptyVsNonEmpty;
		ratioEmptyVsNonEmpty = this.finderService.findRatioEmptyVsNonEmpty();

		// Req Acme-Parade 8.1
		Double ratioAreaWithoutChapter;
		Double[] findDataNumerParadesCoordinatedByChapters;
		Collection<Chapter> chaptersCoordinateLeast10MoreParadasThanAverage;
		Double findRatioParadesDraftModeVSParadesFinalMode;
		ratioAreaWithoutChapter = this.areaService.ratioAreaWithoutChapter();
		findDataNumerParadesCoordinatedByChapters = this.paradeService.findDataNumerParadesCoordinatedByChapters();
		chaptersCoordinateLeast10MoreParadasThanAverage = this.chapterService.chaptersCoordinateLeast10MoreParadasThanAverage();
		findRatioParadesDraftModeVSParadesFinalMode = this.paradeService.findRatioParadesDraftModeVSParadesFinalMode();

		// Req Acme-Parade 8.1.5
		Double findRatioSubmittedParadesFinalMode;
		Double findRatioAcceptedParadesFinalMode;
		Double findRatioRejectedParadesFinalMode;
		findRatioSubmittedParadesFinalMode = this.paradeService.findRatioSubmittedParadesFinalMode();
		findRatioAcceptedParadesFinalMode = this.paradeService.findRatioAcceptedParadesFinalMode();
		findRatioRejectedParadesFinalMode = this.paradeService.findRatioRejectedParadesFinalMode();

		// LEVEL B --------------------------------------

		// Req Acme-Parade 18.2
		Double ratioActiveSponsorship;
		Double[] dataSponsorshipPerSponsor;
		Collection<Sponsor> topFiveSponsors;
		ratioActiveSponsorship = this.sponsorshipService.ratioActiveSponsorship();
		dataSponsorshipPerSponsor = this.sponsorshipService.dataSponsorshipPerSponsor();
		topFiveSponsors = this.sponsorService.topFiveSponsors();

		result = new ModelAndView("dashboard/display");

		// LEVEL C
		result.addObject("dataMembersPerBrotherhood", dataMembersPerBrotherhood);
		result.addObject("largestBrotherhoods", largestBrotherhoods);
		result.addObject("smallestBrotherhoods", smallestBrotherhoods);
		result.addObject("parades", parades);
		result.addObject("histogramValues", histogramValues);
		result.addObject("histogramLabels", histogramLabels);
		result.addObject("pendingRatio", pendingRatio);
		result.addObject("approvedRatio", approvedRatio);
		result.addObject("rejectedRatio", rejectedRatio);
		result.addObject("members", members);
		result.addObject("mapa", ratioRequestByParade);
		result.addObject("findBrotherhoohLargestHistory", findBrotherhoohLargestHistory);
		result.addObject("findDataNumberRecordsPerHistory", findDataNumberRecordsPerHistory);
		result.addObject("findBrotherhoohsLargestHistoryAvg", findBrotherhoohsLargestHistoryAvg);

		// LEVEL B
		result.addObject("dataResultsPerFinder", dataResultsPerFinder);
		result.addObject("ratioEmptyVsNonEmpty", ratioEmptyVsNonEmpty);
		result.addObject("dataBrotherhoodPerArea", dataBrotherhoodPerArea);
		result.addObject("countBrotherhoodsPerArea", countBrotherhoodsPerArea);
		result.addObject("ratioBrotherhoodsPerArea", ratioBrotherhoodsPerArea);
		result.addObject("ratioAreaWithoutChapter", ratioAreaWithoutChapter);
		result.addObject("findDataNumerParadesCoordinatedByChapters", findDataNumerParadesCoordinatedByChapters);
		result.addObject("chaptersCoordinateLeast10MoreParadasThanAverage", chaptersCoordinateLeast10MoreParadasThanAverage);
		result.addObject("findRatioParadesDraftModeVSParadesFinalMode", findRatioParadesDraftModeVSParadesFinalMode);
		result.addObject("findRatioSubmittedParadesFinalMode", findRatioSubmittedParadesFinalMode);
		result.addObject("findRatioAcceptedParadesFinalMode", findRatioAcceptedParadesFinalMode);
		result.addObject("findRatioRejectedParadesFinalMode", findRatioRejectedParadesFinalMode);

		// LEVEL A
		result.addObject("ratioActiveSponsorship", ratioActiveSponsorship);
		result.addObject("dataSponsorshipPerSponsor", dataSponsorshipPerSponsor);
		result.addObject("topFiveSponsors", topFiveSponsors);

		return result;
	}

}
