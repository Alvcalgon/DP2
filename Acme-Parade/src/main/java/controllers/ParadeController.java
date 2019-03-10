
package controllers;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.BrotherhoodService;
import services.ChapterService;
import services.FloatService;
import services.MemberService;
import services.ParadeService;
import services.RequestService;
import services.UtilityService;
import domain.Area;
import domain.Brotherhood;
import domain.Chapter;
import domain.Member;
import domain.Parade;

@Controller
@RequestMapping(value = "/parade")
public class ParadeController extends AbstractController {

	@Autowired
	private ParadeService		paradeService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private MemberService		memberService;

	@Autowired
	private UtilityService		utilityService;

	@Autowired
	private RequestService		requestService;

	@Autowired
	private FloatService		floatService;

	@Autowired
	private ChapterService		chapterService;


	// Constructors -----------------------------------------------------------

	public ParadeController() {
		super();
	}

	// Parade display ---------------------------------------------------------------		

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int paradeId) {
		ModelAndView result;
		Parade parade;
		Brotherhood brotherhood;
		Brotherhood principal;
		Collection<domain.Float> floats;

		result = new ModelAndView("parade/display");

		try {
			brotherhood = this.brotherhoodService.findBrotherhoodByParade(paradeId);

			//Está registrado como hermandad y además es el dueño de la desfile
			if (LoginService.getPrincipal().getAuthorities().toString().equals("[BROTHERHOOD]") && brotherhood.getId() == this.brotherhoodService.findByPrincipal().getId()) {
				principal = this.brotherhoodService.findByPrincipal();
				parade = this.paradeService.findOne(paradeId);

				result.addObject("isOwner", true);
				result.addObject("principal", principal);
				result.addObject("parade", parade);
				result.addObject("floats", parade.getFloats());
				result.addObject("segments", parade.getSegments());

			} else if (LoginService.getPrincipal().getAuthorities().toString().equals("[CHAPTER]") && brotherhood.getArea() == this.chapterService.findByPrincipal().getArea()) {
				parade = this.paradeService.findOneToDisplayToChapter(paradeId);
				brotherhood = this.brotherhoodService.findBrotherhoodByParade(paradeId);

				result.addObject("parade", parade);
				result.addObject("floats", parade.getFloats());
				result.addObject("segments", parade.getSegments());
			} else {
				parade = this.paradeService.findOneToDisplay(paradeId);
				brotherhood = this.brotherhoodService.findBrotherhoodByParade(paradeId);
				floats = parade.getFloats();
				result.addObject("parade", parade);
				result.addObject("floats", parade.getFloats());
				result.addObject("segments", parade.getSegments());

				if (LoginService.getPrincipal().getAuthorities().toString().equals("[MEMBER]"))
					this.isRequestable(parade, result);
			}

			result.addObject("brotherhood", brotherhood);

		} catch (final Exception e1) {

			try {
				parade = this.paradeService.findOneToDisplay(paradeId);
				brotherhood = this.brotherhoodService.findBrotherhoodByParade(paradeId);
				floats = parade.getFloats();

				result.addObject("parade", parade);
				result.addObject("floats", floats);
				result.addObject("segments", parade.getSegments());
				result.addObject("brotherhood", brotherhood);
			} catch (final Exception e) {

				result = new ModelAndView("redirect:../error.do");
			}
		}

		return result;
	}
	// Parade list ---------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int brotherhoodId) {
		ModelAndView result;
		Collection<Parade> parades;
		Collection<Parade> paradesSubmittedBrotherhood;
		Collection<Parade> paradesRejectedBrotherhood;
		Collection<Parade> paradesAcceptedBrotherhood;
		Collection<Parade> paradesSubmittedFinal;
		Collection<Parade> paradesRejectedFinal;
		Collection<Parade> paradesAcceptedFinal;
		Brotherhood principal;
		Boolean hasFloats;
		final Area areaBrotherhood;
		final Area areaChapter;
		Brotherhood brotherhood;
		Chapter chapterprincipal;

		result = new ModelAndView("parade/list");
		parades = this.paradeService.findParadeAcceptedFinalByBrotherhood(brotherhoodId);
		result.addObject("paradesAccepted", parades);
		result.addObject("brotherhoodId", brotherhoodId);

		try {
			if (LoginService.getPrincipal().getAuthorities().toString().equals("[BROTHERHOOD]")) {

				principal = this.brotherhoodService.findByPrincipal();
				hasFloats = this.floatService.findFloatByBrotherhood(principal.getId()).isEmpty();
				if (principal.getArea() != null)
					result.addObject("areaSelected", true);
				result.addObject("hasFloats", hasFloats);
				result.addObject("brotherhoodId", principal.getId());
				result.addObject("principal", principal);

				if (brotherhoodId == this.brotherhoodService.findByPrincipal().getId()) {

					paradesSubmittedBrotherhood = this.paradeService.findParadeSubmittedByBrotherhood(principal.getId());
					paradesRejectedBrotherhood = this.paradeService.findParadeRejectedByBrotherhood(principal.getId());
					paradesAcceptedBrotherhood = this.paradeService.findParadeAcceptedByBrotherhood(principal.getId());

					result.addObject("isOwner", true);
					result.addObject("paradesSubmitted", paradesSubmittedBrotherhood);
					result.addObject("paradesRejected", paradesRejectedBrotherhood);
					result.addObject("paradesAccepted", paradesAcceptedBrotherhood);

				}
			} else if (LoginService.getPrincipal().getAuthorities().toString().equals("[CHAPTER]")) {

				brotherhood = this.brotherhoodService.findOne(brotherhoodId);
				chapterprincipal = this.chapterService.findByPrincipal();
				if (brotherhood.getArea().equals(chapterprincipal.getArea())) {

					areaChapter = this.chapterService.findByPrincipal().getArea();
					areaBrotherhood = this.brotherhoodService.findOne(brotherhoodId).getArea();

					paradesSubmittedFinal = this.paradeService.findParadeSubmittedFinalByBrotherhood(brotherhoodId);
					paradesRejectedFinal = this.paradeService.findParadeRejectedFinalByBrotherhood(brotherhoodId);
					paradesAcceptedFinal = this.paradeService.findParadeAcceptedFinalByBrotherhood(brotherhoodId);

					result.addObject("paradesSubmitted", paradesSubmittedFinal);
					result.addObject("paradesRejected", paradesRejectedFinal);
					result.addObject("paradesAccepted", paradesAcceptedFinal);
					result.addObject("isChapterOwner", areaChapter.equals(areaBrotherhood));
				}
			}

		} catch (final Exception e) {
		}

		result.addObject("requestURI", "parade/list.do?brotherhoodId=" + brotherhoodId);
		return result;

	}
	//Este método se usa en caso de que si es un miembro para que pueda solicitar salir en la desfile
	private void isRequestable(final Parade parade, final ModelAndView result) {
		Collection<Member> members;
		Brotherhood brotherhood;
		Date dateNow;

		brotherhood = this.brotherhoodService.findBrotherhoodByParade(parade.getId());
		members = this.memberService.findEnroledMemberByBrotherhood(brotherhood.getId());
		dateNow = this.utilityService.current_moment();

		if (members.contains(this.memberService.findByPrincipal()))
			if (this.requestService.findRequestMemberParade(this.memberService.findByPrincipal().getId(), parade.getId()).isEmpty())
				if (dateNow.before(this.paradeService.findOne(parade.getId()).getMoment()))
					result.addObject("memberAutorize", true);
	}
}
