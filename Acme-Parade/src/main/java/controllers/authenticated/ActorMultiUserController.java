
package controllers.authenticated;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AdministratorService;
import services.BrotherhoodService;
import services.ChapterService;
import services.MemberService;
import services.SponsorService;
import controllers.ActorAbstractController;
import domain.Actor;
import domain.Administrator;
import domain.Brotherhood;
import domain.Chapter;
import domain.Member;
import domain.Sponsor;
import forms.BrotherhoodRegistrationForm;
import forms.ChapterRegistrationForm;
import forms.RegistrationForm;

@Controller
@RequestMapping(value = "/actor/administrator,brotherhood,chapter,member,sponsor")
public class ActorMultiUserController extends ActorAbstractController {

	// Services

	@Autowired
	private ActorService			actorService;

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private MemberService			memberService;

	@Autowired
	private ChapterService			chapterService;

	@Autowired
	private SponsorService			sponsorService;


	// Constructor

	public ActorMultiUserController() {
		super();
	}

	// Edit

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int actorId) {
		ModelAndView result;
		Actor actor;
		final Brotherhood brotherhood;
		Administrator administrator;
		Member member;
		Chapter chapter;
		final Sponsor sponsor;

		result = new ModelAndView();

		try {
			actor = this.actorService.findOneToDisplayEdit(actorId);
			if (actor instanceof Member) {
				member = this.memberService.findOneToDisplayEdit(actorId);
				result = this.createModelAndView(member);
				result.addObject("rol", "Member");

			} else if (actor instanceof Administrator) {
				administrator = this.administratorService.findOneToDisplayEdit(actorId);
				result = this.createModelAndView(administrator);
				result.addObject("rol", "Administrator");
			} else if (actor instanceof Brotherhood) {
				brotherhood = this.brotherhoodService.findOneToDisplayEdit(actorId);
				result = this.createModelAndView(brotherhood);
				result.addObject("rol", "Brotherhood");
			} else if (actor instanceof Chapter) {
				chapter = this.chapterService.findOneToDisplayEdit(actorId);
				result = this.createModelAndView(chapter);
				result.addObject("rol", "Chapter");
			} else if (actor instanceof Sponsor) {
				sponsor = this.sponsorService.findOneToDisplayEdit(actorId);
				result = this.createModelAndView(sponsor);
				result.addObject("rol", "Sponsor");
			}

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/error.do");
		}

		return result;

	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveAdmin")
	public ModelAndView saveAdministrator(final RegistrationForm registrationForm, final BindingResult binding) {
		ModelAndView result;
		Administrator administrator;

		administrator = this.administratorService.reconstruct(registrationForm, binding);

		if (binding.hasErrors()) {
			result = this.createModelAndView(registrationForm);
			result.addObject("rol", "Administrator");
		} else
			try {
				this.administratorService.save(administrator);
				result = new ModelAndView("redirect:/actor/display.do");
			} catch (final Throwable oops) {
				result = this.createModelAndView(registrationForm, "actor.commit.error");
				result.addObject("rol", "Administrator");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "deleteAdmin")
	public ModelAndView deleteAdministrator(final RegistrationForm registrationForm, final BindingResult binding, final HttpSession session) {
		ModelAndView result;
		Administrator administrator;

		administrator = this.administratorService.reconstruct(registrationForm, binding);

		if (binding.hasErrors()) {
			result = this.createModelAndView(registrationForm);
			result.addObject("rol", "Administrator");
		} else
			try {
				this.administratorService.delete(administrator);
				session.invalidate();
				result = new ModelAndView("/welcome/index");
			} catch (final Throwable oops) {
				result = this.createModelAndView(registrationForm, "actor.commit.error");
				result.addObject("rol", "Administrator");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveBrotherhood")
	public ModelAndView saveBrotherhood(@ModelAttribute("registrationForm") final BrotherhoodRegistrationForm brotherhoodRegistrationForm, final BindingResult binding) {
		ModelAndView result;
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.reconstruct(brotherhoodRegistrationForm, binding);

		if (binding.hasErrors()) {
			result = this.createModelAndView(brotherhoodRegistrationForm);
			result.addObject("rol", "Brotherhood");
		} else
			try {
				this.brotherhoodService.save(brotherhood);
				result = new ModelAndView("redirect:/actor/display.do");
			} catch (final Throwable oops) {
				result = this.createModelAndView(brotherhoodRegistrationForm, "actor.commit.error");
				result.addObject("rol", "Brotherhood");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "deleteBrotherhood")
	public ModelAndView deleteBrotherhood(@ModelAttribute("registrationForm") final BrotherhoodRegistrationForm brotherhoodRegistrationForm, final BindingResult binding, final HttpSession session) {
		ModelAndView result;
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.reconstruct(brotherhoodRegistrationForm, binding);

		if (binding.hasErrors()) {
			result = this.createModelAndView(brotherhoodRegistrationForm);
			result.addObject("rol", "Brotherhood");
		} else
			try {
				this.brotherhoodService.delete(brotherhood);
				session.invalidate();
				result = new ModelAndView("welcome/index");
			} catch (final Throwable oops) {
				result = this.createModelAndView(brotherhoodRegistrationForm, "actor.commit.error");
				result.addObject("rol", "Brotherhood");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveMember")
	public ModelAndView saveMember(final RegistrationForm registrationForm, final BindingResult binding) {
		ModelAndView result;
		Member member;

		member = this.memberService.reconstruct(registrationForm, binding);

		if (binding.hasErrors()) {
			result = this.createModelAndView(registrationForm);
			result.addObject("rol", "Member");
		} else
			try {
				this.memberService.save(member);
				result = new ModelAndView("redirect:/actor/display.do");
			} catch (final Throwable oops) {
				result = this.createModelAndView(registrationForm, "actor.commit.error");
				result.addObject("rol", "Member");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "deleteMember")
	public ModelAndView deleteMember(final RegistrationForm registrationForm, final BindingResult binding, final HttpSession session) {
		ModelAndView result;
		Member member;

		member = this.memberService.reconstruct(registrationForm, binding);

		if (binding.hasErrors()) {
			result = this.createModelAndView(registrationForm);
			result.addObject("rol", "Member");
		} else
			try {
				this.memberService.delete(member);
				session.invalidate();
				result = new ModelAndView("welcome/index");
			} catch (final Throwable oops) {
				result = this.createModelAndView(registrationForm, "actor.commit.error");
				result.addObject("rol", "Member");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveChapter")
	public ModelAndView saveChapter(@ModelAttribute("registrationForm") final ChapterRegistrationForm chapterRegistrationForm, final BindingResult binding) {
		ModelAndView result;
		Chapter chapter;

		chapter = this.chapterService.reconstruct(chapterRegistrationForm, binding);

		if (binding.hasErrors()) {
			result = this.createModelAndView(chapterRegistrationForm);
			result.addObject("rol", "Chapter");
		} else
			try {
				this.chapterService.save(chapter);
				result = new ModelAndView("redirect:/actor/display.do");
			} catch (final Throwable oops) {
				result = this.createModelAndView(chapterRegistrationForm, "actor.commit.error");
				result.addObject("rol", "Chapter");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "deleteChapter")
	public ModelAndView deleteChapter(@ModelAttribute("registrationForm") final ChapterRegistrationForm chapterRegistrationForm, final BindingResult binding, final HttpSession session) {
		ModelAndView result;
		Chapter chapter;

		chapter = this.chapterService.reconstruct(chapterRegistrationForm, binding);

		if (binding.hasErrors()) {
			result = this.createModelAndView(chapterRegistrationForm);
			result.addObject("rol", "Chapter");
		} else
			try {
				this.chapterService.delete(chapter);
				session.invalidate();
				result = new ModelAndView("welcome/index");
			} catch (final Throwable oops) {
				result = this.createModelAndView(chapterRegistrationForm, "actor.commit.error");
				result.addObject("rol", "Chapter");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveSponsor")
	public ModelAndView saveSponsor(final RegistrationForm registrationForm, final BindingResult binding) {
		ModelAndView result;
		Sponsor sponsor;

		sponsor = this.sponsorService.reconstruct(registrationForm, binding);

		if (binding.hasErrors()) {
			result = this.createModelAndView(registrationForm);
			result.addObject("rol", "Sponsor");
		} else
			try {
				this.sponsorService.save(sponsor);
				result = new ModelAndView("redirect:/actor/display.do");
			} catch (final Throwable oops) {
				result = this.createModelAndView(registrationForm, "actor.commit.error");
				result.addObject("rol", "Sponsor");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "deleteSponsor")
	public ModelAndView deleteSponsor(final RegistrationForm registrationForm, final BindingResult binding, final HttpSession session) {
		ModelAndView result;
		Sponsor sponsor;

		sponsor = this.sponsorService.reconstruct(registrationForm, binding);

		if (binding.hasErrors()) {
			result = this.createModelAndView(registrationForm);
			result.addObject("rol", "Sponsor");
		} else
			try {
				this.sponsorService.delete(sponsor);
				session.invalidate();
				result = new ModelAndView("welcome/index");
			} catch (final Throwable oops) {
				result = this.createModelAndView(registrationForm, "actor.commit.error");
				result.addObject("rol", "Sponsor");
			}

		return result;
	}

	// Ancillary methods

	protected ModelAndView createModelAndView(final Administrator administrator) {
		ModelAndView result;
		RegistrationForm registrationForm;

		registrationForm = this.administratorService.createForm(administrator);

		result = this.createModelAndView(registrationForm, null);

		return result;
	}

	protected ModelAndView createModelAndView(final Member member) {
		ModelAndView result;
		RegistrationForm registrationForm;

		registrationForm = this.memberService.createForm(member);

		result = this.createModelAndView(registrationForm, null);

		return result;
	}

	protected ModelAndView createModelAndView(final Brotherhood brotherhood) {
		ModelAndView result;
		BrotherhoodRegistrationForm brotherhoodRegistrationForm;

		brotherhoodRegistrationForm = this.brotherhoodService.createForm(brotherhood);

		result = this.createModelAndView(brotherhoodRegistrationForm, null);

		return result;
	}

	protected ModelAndView createModelAndView(final Chapter chapter) {
		ModelAndView result;
		ChapterRegistrationForm chapterRegistrationForm;

		chapterRegistrationForm = this.chapterService.createForm(chapter);

		result = this.createModelAndView(chapterRegistrationForm, null);

		return result;
	}

	protected ModelAndView createModelAndView(final Sponsor sponsor) {
		ModelAndView result;
		RegistrationForm registrationForm;

		registrationForm = this.sponsorService.createForm(sponsor);

		result = this.createModelAndView(registrationForm, null);

		return result;
	}

	protected ModelAndView createModelAndView(final RegistrationForm registrationForm) {
		ModelAndView result;

		result = this.createModelAndView(registrationForm, null);

		return result;
	}

	protected ModelAndView createModelAndView(final RegistrationForm registrationForm, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/edit");
		result.addObject("registrationForm", registrationForm);
		result.addObject("messageCode", messageCode);

		return result;
	}

	protected ModelAndView createModelAndView(final ChapterRegistrationForm chapterRegistrationForm) {
		ModelAndView result;

		result = this.createModelAndView(chapterRegistrationForm, null);

		return result;
	}

	protected ModelAndView createModelAndView(final ChapterRegistrationForm chapterRegistrationForm, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/edit");
		result.addObject("registrationForm", chapterRegistrationForm);
		result.addObject("messageCode", messageCode);

		return result;
	}

	protected ModelAndView createModelAndView(final BrotherhoodRegistrationForm brotherhoodRegistrationForm) {
		ModelAndView result;

		result = this.createModelAndView(brotherhoodRegistrationForm, null);

		return result;
	}

	protected ModelAndView createModelAndView(final BrotherhoodRegistrationForm brotherhoodRegistrationForm, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/edit");
		result.addObject("registrationForm", brotherhoodRegistrationForm);
		result.addObject("messageCode", messageCode);
		result.addObject("role", "brotherhood");

		return result;
	}

}
