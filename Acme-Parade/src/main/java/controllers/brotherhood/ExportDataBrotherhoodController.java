
package controllers.brotherhood;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import services.BoxService;
import services.BrotherhoodService;
import services.FloatService;
import services.MessageService;
import services.ParadeService;
import services.SocialProfileService;
import controllers.AbstractController;
import domain.Actor;
import domain.Box;
import domain.Brotherhood;
import domain.Float;
import domain.Message;
import domain.Parade;
import domain.SocialProfile;

@Controller
@RequestMapping(value = "/exportData/brotherhood")
public class ExportDataBrotherhoodController extends AbstractController {

	@Autowired
	private BrotherhoodService		brotherhoodService;
	@Autowired
	private SocialProfileService	socialProfileService;
	@Autowired
	private MessageService			messageService;
	@Autowired
	private BoxService				boxService;
	@Autowired
	private FloatService			floatService;
	@Autowired
	private ParadeService			paradeService;


	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public void test(final HttpSession session, final HttpServletResponse response) throws IOException {
		String data = "Data of your user account:\r\n";

		final Brotherhood brotherhood = this.brotherhoodService.findByPrincipal();
		final Collection<SocialProfile> socialProfiles = this.socialProfileService.findSocialProfilesByActor(brotherhood.getId());
		final Collection<Message> messagesSent = this.messageService.findSentMessagesByActor(brotherhood.getId());
		final Collection<Message> messagesReceived = this.messageService.findReceivedMessagesByActor(brotherhood.getId());
		final Collection<Box> boxs = this.boxService.findBoxesByActor(brotherhood.getId());
		final Collection<Float> floats = this.floatService.findFloatByBrotherhood(brotherhood.getId());
		final Collection<Parade> parades = this.paradeService.findParadeByBrotherhood(brotherhood.getId());

		data += "\r\n\r\n";

		data += brotherhood.getName() + " " + brotherhood.getMiddleName() + " " + brotherhood.getSurname() + " " + brotherhood.getPhoto() + " " + brotherhood.getEmail() + " " + brotherhood.getPhoneNumber() + " " + brotherhood.getAddress() + " " + " \r\n";
		data += "\r\n\r\n";
		data += "Social Profiles:\r\n";
		for (final SocialProfile socialProfile : socialProfiles)
			data += socialProfile.getNick() + " " + socialProfile.getLinkProfile() + " " + socialProfile.getSocialNetwork() + "\r\n";
		data += "\r\n\r\n";
		data += "Boxes:\r\n";
		for (final Box box : boxs)
			data += "Box name: " + box.getName() + "\r\n";
		data += "\r\n\r\n";
		data += "Sent Messages:\r\n\r\n";
		for (final Message messages : messagesSent)
			data += "Sender: " + messages.getSender().getName() + " " + messages.getSender().getSurname() + " Recipient: " + ((Actor) messages.getRecipients()).getName() + " Sent Moment: " + messages.getSentMoment() + " Subject: " + messages.getSubject()
				+ " Body: " + messages.getBody() + " Tags: " + messages.getTags() + " Priority: " + messages.getPriority() + "\r\n";
		data += "\r\n\r\n";
		data += "Received Messages:\r\n\r\n";
		for (final Message messages : messagesReceived)
			data += "Sender: " + messages.getSender().getName() + " " + messages.getSender().getSurname() + " Recipient: " + ((Actor) messages.getRecipients()).getName() + " Sent Moment: " + messages.getSentMoment() + " Subject: " + messages.getSubject()
				+ " Body: " + messages.getBody() + " Tags: " + messages.getTags() + " Priority: " + messages.getPriority() + "\r\n";
		data += "\r\n\r\n";
		data += "Floats:\r\n\r\n";
		for (final Float flooat : floats)
			data += "Title: " + flooat.getTitle() + " Description: " + flooat.getDescription() + " Pictures: " + flooat.getPictures() + "\r\n";
		data += "\r\n\r\n";
		data += "Processions:\r\n\r\n";
		for (final Parade parade : parades)
			data += "Parade title: " + parade.getTitle() + " Description: " + parade.getDescription() + " Moment: " + parade.getMoment() + " Final Mode: " + parade.getIsFinalMode() + " Status: " + parade.getStatus() + " Reason Why: "
				+ parade.getReasonWhy() + "\r\n";

		response.setContentType("text/plain");
		response.setHeader("Content-Disposition", "attachment;filename=data_user_account.txt");
		final ServletOutputStream out = response.getOutputStream();
		out.println(data);
		out.flush();
		out.close();
	}

}
