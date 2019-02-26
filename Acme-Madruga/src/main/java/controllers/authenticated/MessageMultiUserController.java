
package controllers.authenticated;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.BoxService;
import services.CustomisationService;
import services.MessageService;
import controllers.AbstractController;
import domain.Actor;
import domain.Box;
import domain.Customisation;
import domain.Message;

@Controller
@RequestMapping("/message/administrator,brotherhood,member")
public class MessageMultiUserController extends AbstractController {

	@Autowired
	private MessageService			messageService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private BoxService				boxService;

	@Autowired
	private CustomisationService	customisationService;


	public MessageMultiUserController() {
		super();
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int messageId, @RequestParam final int boxId) {
		ModelAndView result;
		Message message;

		try {
			message = this.messageService.findOneToDisplay(messageId);

			result = new ModelAndView("message/display");
			result.addObject("boxId", boxId);
			result.addObject("messageToDisplay", message);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:error.do");
		}

		return result;
	}

	@RequestMapping(value = "/send", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Message message;

		message = this.messageService.create();

		result = this.createEditModelAndView(message);

		return result;

	}

	@RequestMapping(value = "/send", method = RequestMethod.POST, params = "send")
	public ModelAndView send(@Valid final Message message, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(message);
		else
			try {
				this.messageService.send(message);
				result = new ModelAndView("redirect:/box/administrator,brotherhood,member/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(message, "message.commit.error");
			}

		return result;

	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int messageId, @RequestParam final int boxId) {
		ModelAndView result;
		Message message;
		Box box;

		box = this.boxService.findOne(boxId);
		message = this.messageService.findOne(messageId);

		try {
			this.messageService.delete(message, box);
			result = new ModelAndView("redirect:/box/administrator,brotherhood,member/list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(message, "message.commit.error");
		}

		return result;

	}

	// Ancillary methods ----------------------------------
	protected ModelAndView createEditModelAndView(final Message message) {
		ModelAndView result;

		result = this.createEditModelAndView(message, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Message message, final String messageCode) {
		ModelAndView result;
		Collection<Actor> actors;
		Actor principal;
		Customisation customisation;

		customisation = this.customisationService.find();

		principal = this.actorService.findPrincipal();
		actors = this.actorService.findAll();
		actors.remove(principal);

		result = new ModelAndView("message/send");
		result.addObject("message", message);
		result.addObject("actors", actors);
		result.addObject("priorities", customisation.getPriorities());
		result.addObject("messageCode", messageCode);

		return result;

	}

}
