
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.MemberService;
import domain.Member;

@Controller
@RequestMapping(value = "/member")
public class MemberController extends AbstractController {

	// Services

	@Autowired
	private MemberService	memberService;


	// Constructor

	public MemberController() {
		super();
	}

	// List

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int brotherhoodId) {
		ModelAndView result;
		Collection<Member> members;

		members = this.memberService.findEnroledMemberByBrotherhood(brotherhoodId);

		result = new ModelAndView("actor/list");

		result.addObject("actors", members);
		result.addObject("requestURI", "member/list.do");
		result.addObject("brotherhoodId", brotherhoodId);
		return result;
	}

}
