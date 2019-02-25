
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
import services.MemberService;
import services.ProcessionService;
import services.RequestService;
import services.UtilityService;
import domain.Brotherhood;
import domain.Member;
import domain.Procession;

@Controller
@RequestMapping(value = "/procession")
public class ProcessionController extends AbstractController {

	@Autowired
	private ProcessionService	processionService;

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private MemberService		memberService;

	@Autowired
	private RequestService		requestService;

	@Autowired
	private UtilityService		utilityService;


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
		Collection<Member> members;
		Date dateNow;

		result = new ModelAndView("procession/display");
		brotherhood = this.brotherhoodService.findBrotherhoodByProcession(processionId);
		members = this.memberService.findEnroledMemberByBrotherhood(brotherhood.getId());
		dateNow = this.utilityService.current_moment();

		try {
			if (LoginService.getPrincipal().getAuthorities().toString().equals("[BROTHERHOOD]") && brotherhood.getId() == this.brotherhoodService.findByPrincipal().getId()) {

				principal = this.brotherhoodService.findByPrincipal();

				result.addObject("isOwner", true);
				result.addObject("principal", principal);

			}
			if (LoginService.getPrincipal().getAuthorities().toString().equals("[MEMBER]"))
				if (members.contains(this.memberService.findByPrincipal())) {
					if (this.requestService.findRequestMemberProcession(this.memberService.findByPrincipal().getId(), processionId).isEmpty())
						if (dateNow.before(this.processionService.findOne(processionId).getMoment()))
							result.addObject("memberAutorize", true);
				} else
					result.addObject("memberAutorize", false);

			procession = this.processionService.findOne(processionId);
			floats = procession.getFloats();

			result.addObject("procession", procession);
			result.addObject("floats", floats);
			result.addObject("brotherhood", brotherhood);
			result.addObject("dateNow", dateNow);

		} catch (final Exception e) {
			try {
				procession = this.processionService.findOneToDisplay(processionId);
				floats = procession.getFloats();

				result.addObject("procession", procession);
				result.addObject("floats", floats);
				result.addObject("brotherhood", brotherhood);

			} catch (final Exception e1) {
				result = new ModelAndView("redirect:../error.do");
			}

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
					result.addObject("brotherhoodId", principal.getId());
					if (principal.getArea() == null)
						result.addObject("areaSelected", false);
					else
						result.addObject("areaSelected", true);
				}
		} catch (final Exception e) {
		}

		result.addObject("requestURI", "procession/list.do?brotherhoodId=" + brotherhoodId);
		return result;

	}
}
