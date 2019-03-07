
package controllers.authenticated;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.BoxService;
import controllers.AbstractController;
import domain.Actor;
import domain.Box;
import domain.Message;

@Controller
@RequestMapping("/box/administrator,brotherhood,member")
public class BoxMultiUserController extends AbstractController {

	@Autowired
	private BoxService		boxService;

	@Autowired
	private ActorService	actorService;


	public BoxMultiUserController() {
		super();
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int boxId) {
		ModelAndView result;
		Box box;
		Collection<Box> childBoxes;
		Collection<Message> messages;

		try {
			box = this.boxService.findOneToDisplay(boxId);
			childBoxes = this.boxService.findChildBoxesByBox(boxId);
			messages = box.getMessages();

			result = new ModelAndView("box/display");
			result.addObject("box", box);
			result.addObject("childBoxes", childBoxes);
			result.addObject("messages", messages);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/error.do");
		}

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Box> boxes;
		Actor principal;

		principal = this.actorService.findPrincipal();
		boxes = this.boxService.findRootBoxesByActor(principal.getId());

		result = new ModelAndView("box/list");
		result.addObject("boxes", boxes);
		result.addObject("requestURI", "box/administrator,brotherhood,member/list.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Box box;

		box = this.boxService.create();

		result = this.createEditModelAndView(box);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int boxId) {
		ModelAndView result;
		Box box;

		try {
			box = this.boxService.findOneToEdit(boxId);

			result = this.createEditModelAndView(box);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/error.do");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Box box, final BindingResult binding) {
		ModelAndView result;
		Box boxRec;

		boxRec = this.boxService.reconstruct(box, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(box);
		else
			try {
				this.boxService.save(boxRec);
				result = new ModelAndView("redirect:list.do");
			} catch (final DataIntegrityViolationException oops) {
				result = this.createEditModelAndView(boxRec, "box.name.unique");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(boxRec, "box.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Box box, final BindingResult binding) {
		ModelAndView result;
		Box deleted;

		deleted = this.boxService.findOne(box.getId());
		try {
			this.boxService.delete(deleted);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(box, "box.commit.error");
		}

		return result;
	}

	// Ancillary methods --------------------------
	protected ModelAndView createEditModelAndView(final Box box) {
		ModelAndView result;

		result = this.createEditModelAndView(box, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Box box, final String messageCode) {
		ModelAndView result;
		Collection<Box> parents;
		Actor principal;

		principal = this.actorService.findPrincipal();
		parents = this.boxService.posibleParentBoxes(box, principal.getId());

		if (box.getId() != 0)
			parents.remove(box);

		result = new ModelAndView("box/edit");
		result.addObject("box", box);
		result.addObject("parents", parents);
		result.addObject("messageCode", messageCode);

		return result;

	}

}
