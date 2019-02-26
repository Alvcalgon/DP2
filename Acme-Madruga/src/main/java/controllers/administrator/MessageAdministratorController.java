
package controllers.administrator;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.CustomisationService;
import services.MessageService;
import controllers.AbstractController;
import domain.Customisation;
import domain.Message;

@Controller
@RequestMapping("/message/administrator")
public class MessageAdministratorController extends AbstractController {

	@Autowired
	private MessageService			messageService;

	@Autowired
	private CustomisationService	customisationService;


	// Constructors -----------------------------------------------------------
	public MessageAdministratorController() {
		super();
	}

	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/broadcast", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Message broadcast;

		broadcast = this.messageService.createBroadcast();

		result = this.broadcastModelAndView(broadcast);

		return result;
	}

	// Edition ----------------------------------------------------------------

	@RequestMapping(value = "/broadcast", method = RequestMethod.POST, params = "send")
	public ModelAndView broadcast(@Valid final Message broadcast, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.broadcastModelAndView(broadcast);
		else
			try {
				this.messageService.sendBroadcast(broadcast);
				result = new ModelAndView("redirect:/box/administrator,brotherhood,member/list.do");
			} catch (final Throwable oops) {
				result = this.broadcastModelAndView(broadcast, "message.commit.error");
			}

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView broadcastModelAndView(final Message broadcast) {
		ModelAndView result;

		result = this.broadcastModelAndView(broadcast, null);

		return result;
	}

	protected ModelAndView broadcastModelAndView(final Message broadcast, final String messageCode) {
		ModelAndView result;
		Customisation customisation;

		customisation = this.customisationService.find();

		result = new ModelAndView("message/send");
		result.addObject("message", broadcast);
		result.addObject("priorities", customisation.getPriorities());
		result.addObject("isBroadcastMessage", true);
		result.addObject("actionURI", "message/administrator/broadcast.do");
		result.addObject("messageCode", messageCode);

		return result;
	}

}
