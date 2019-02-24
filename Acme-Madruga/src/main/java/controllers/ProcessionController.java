
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
import services.MemberService;
import services.ProcessionService;
import services.RequestService;
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
		brotherhood = this.brotherhoodService.findBrotherhoodByProcession(processionId);

		try {
			if (LoginService.getPrincipal().getAuthorities().toString().equals("[BROTHERHOOD]") && brotherhood.getId() == this.brotherhoodService.findByPrincipal().getId()) {

				principal = this.brotherhoodService.findByPrincipal();

				result.addObject("isOwner", true);
				result.addObject("principal", principal);

			}
			procession = this.processionService.findOne(processionId);
			floats = procession.getFloats();

			result.addObject("procession", procession);
			result.addObject("floats", floats);
			result.addObject("brotherhood", brotherhood);

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
		Collection<Member> members;

		result = new ModelAndView("procession/list");
		members = this.memberService.findEnroledMemberByBrotherhood(brotherhoodId);
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
			if (LoginService.getPrincipal().getAuthorities().toString().equals("[MEMBER]"))
				if (members.contains(this.memberService.findByPrincipal()))
					result.addObject("memberAutorize", true);
				else
					result.addObject("memberAutorize", false);
		} catch (final Exception e) {
		}

		result.addObject("requestURI", "procession/list.do?brotherhoodId=" + brotherhoodId);
		return result;

	}
}
