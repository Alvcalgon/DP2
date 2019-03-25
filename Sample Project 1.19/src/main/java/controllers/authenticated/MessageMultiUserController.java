
package controllers.authenticated;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AdministratorService;
import services.BoxService;
import services.CustomisationService;
import services.MessageService;
import services.UtilityService;
import controllers.AbstractController;
import domain.Actor;
import domain.Box;
import domain.Customisation;
import domain.Message;
import forms.MessageForm;

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

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private UtilityService			utilityService;


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
			result = new ModelAndView("redirect:/error.do");
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

	@RequestMapping(value = "/move", method = RequestMethod.GET)
	public ModelAndView move(@RequestParam final int messageId, @RequestParam final int boxId) {
		ModelAndView result;
		MessageForm messageForm;

		try {
			this.messageService.findOneToMove(messageId, boxId);

			messageForm = new MessageForm();
			messageForm.setMessageId(messageId);
			messageForm.setOriginBoxId(boxId);

			result = this.moveModelAndView(messageForm);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/error.do");
		}

		return result;
	}

	@RequestMapping(value = "/move", method = RequestMethod.POST, params = "move")
	public ModelAndView moveMessage(final MessageForm messageForm, final BindingResult binding, final Locale locale) {
		ModelAndView result;
		Message target;
		Box origin, destination;

		target = this.messageService.findOne(messageForm.getMessageId());
		origin = this.boxService.findOne(messageForm.getOriginBoxId());
		destination = this.boxService.findOne(messageForm.getDestinationBoxId());

		this.messageService.validateDestinationBox(messageForm, locale.getLanguage(), binding);
		if (binding.hasErrors())
			result = this.moveModelAndView(messageForm);
		else
			try {
				this.messageService.moveMessage(target, origin, destination);
				result = new ModelAndView("redirect:/box/administrator,brotherhood,member/list.do");
			} catch (final Throwable oops) {
				result = this.moveModelAndView(messageForm, "message.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/send", method = RequestMethod.POST, params = "send")
	public ModelAndView send(final Message message, final BindingResult binding) {
		ModelAndView result;
		Message messageRec;

		messageRec = this.messageService.reconstruct(message, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(message);
		else
			try {
				this.messageService.send(messageRec);
				result = new ModelAndView("redirect:/box/administrator,brotherhood,chapter,member,sponsor/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(messageRec, "message.commit.error");
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
			result = new ModelAndView("redirect:/error.do");
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
		Actor principal, system;
		Customisation customisation;
		String priorities_str;
		List<String> priorities;

		customisation = this.customisationService.find();

		priorities_str = customisation.getPriorities();
		priorities = this.utilityService.ListByString(priorities_str);

		system = this.administratorService.findSystem();
		principal = this.actorService.findPrincipal();
		actors = this.actorService.findAll();
		actors.remove(principal);
		actors.remove(system);

		result = new ModelAndView("message/send");
		result.addObject("message", message);
		result.addObject("actors", actors);
		result.addObject("priorities", priorities);
		result.addObject("isBroadcastMessage", false);
		result.addObject("actionURI", "message/administrator,brotherhood,member/send.do");
		result.addObject("messageCode", messageCode);

		return result;
	}

	protected ModelAndView moveModelAndView(final MessageForm messageForm) {
		ModelAndView result;

		result = this.moveModelAndView(messageForm, null);

		return result;
	}

	protected ModelAndView moveModelAndView(final MessageForm messageForm, final String messageCode) {
		ModelAndView result;
		Collection<Box> boxes;
		Actor principal;

		principal = this.actorService.findPrincipal();
		boxes = this.boxService.findBoxesByActor(principal.getId());

		result = new ModelAndView("message/move");
		result.addObject("messageForm", messageForm);
		result.addObject("destinationBoxes", boxes);
		result.addObject("messageCode", messageCode);

		return result;
	}

}
