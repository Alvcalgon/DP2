
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
	private UtilityService		utilityService;

	@Autowired
	private RequestService		requestService;


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

		result = new ModelAndView("procession/display");

		try {
			brotherhood = this.brotherhoodService.findBrotherhoodByProcession(processionId);

			//Está registrado como hermandad y además es el dueño de la procesión
			if (LoginService.getPrincipal().getAuthorities().toString().equals("[BROTHERHOOD]") && brotherhood.getId() == this.brotherhoodService.findByPrincipal().getId()) {
				principal = this.brotherhoodService.findByPrincipal();
				procession = this.processionService.findOne(processionId);

				result.addObject("isOwner", true);
				result.addObject("principal", principal);

			} else
				procession = this.processionService.findOneToDisplay(processionId);

			brotherhood = this.brotherhoodService.findBrotherhoodByProcession(processionId);
			floats = procession.getFloats();

			result.addObject("procession", procession);
			result.addObject("floats", floats);
			result.addObject("brotherhood", brotherhood);

			if (LoginService.getPrincipal().getAuthorities().toString().equals("[MEMBER]"))
				this.isRequestable(procession, result);

		} catch (final Exception e1) {
			result = new ModelAndView("redirect:../error.do");
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
		result.addObject("brotherhoodId", brotherhoodId);

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

	//Este método se usa en caso de que si es un miembro para que pueda solicitar salir en la procesion
	private void isRequestable(final Procession procession, final ModelAndView result) {
		Collection<Member> members;
		Brotherhood brotherhood;
		Date dateNow;

		brotherhood = this.brotherhoodService.findBrotherhoodByProcession(procession.getId());
		members = this.memberService.findEnroledMemberByBrotherhood(brotherhood.getId());
		dateNow = this.utilityService.current_moment();

		if (members.contains(this.memberService.findByPrincipal()))
			if (this.requestService.findRequestMemberProcession(this.memberService.findByPrincipal().getId(), procession.getId()).isEmpty())
				if (dateNow.before(this.processionService.findOne(procession.getId()).getMoment()))
					result.addObject("memberAutorize", true);
	}
}
