
package controllers.administrator;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import services.AdministratorService;
import services.BoxService;
import services.MessageService;
import services.SocialProfileService;
import controllers.AbstractController;
import domain.Administrator;
import domain.Box;
import domain.Message;
import domain.SocialProfile;

@Controller
@RequestMapping(value = "/exportData/administrator")
public class ExportDataAdministratorController extends AbstractController {

	@Autowired
	private SocialProfileService	socialProfileService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private BoxService				boxService;

	@Autowired
	private AdministratorService	administratorService;


	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public void test(final HttpSession session, final HttpServletResponse response) throws IOException {
		String data = "Data of your user account:\r\n";

		final Administrator admin = this.administratorService.findByPrincipal();
		final Collection<SocialProfile> socialProfiles = this.socialProfileService.findSocialProfilesByActor(admin.getId());
		final Collection<Message> messagesSent = this.messageService.findSentMessagesByActor(admin.getId());
		final Collection<Message> messagesReceived = this.messageService.findReceivedMessagesByActor(admin.getId());
		final Collection<Box> boxs = this.boxService.findBoxesByActor(admin.getId());

		data += "\r\n\r\n";

		data += "Name: " + admin.getName() + " Middle Name: " + admin.getMiddleName() + " Surname: " + admin.getSurname() + " Photo: " + admin.getPhoto() + " Email: " + admin.getEmail() + " Phone Number: " + admin.getPhoneNumber() + " Address "
			+ admin.getAddress() + " \r\n";
		data += "\r\n\r\n";
		data += "Social Profiles:\r\n";
		for (final SocialProfile socialProfile : socialProfiles)
			data += "Nick: " + socialProfile.getNick() + " Link profile: " + socialProfile.getLinkProfile() + " Social Network: " + socialProfile.getSocialNetwork() + "\r\n";
		data += "\r\n\r\n";
		data += "Boxes:\r\n";
		for (final Box box : boxs)
			data += "Name: " + box.getName() + "\r\n";
		data += "\r\n\r\n";
		data += "Sent Messages:\r\n\r\n";
		for (final Message messages : messagesSent)
			data += "Sender: " + messages.getSender().getName() + " Surname: " + messages.getSender().getSurname() + " Sent Moment: " + messages.getSentMoment() + " Subject: " + messages.getSubject() + " Body: " + messages.getBody() + " Tags: "
				+ messages.getTags() + " Priority: " + messages.getPriority() + "\r\n";
		data += "\r\n\r\n";
		data += "Received Messages:\r\n\r\n";
		for (final Message messages : messagesReceived)
			data += "Sender: " + messages.getSender().getName() + " Surname: " + messages.getSender().getSurname() + " Sent Moment: " + messages.getSentMoment() + " Subject: " + messages.getSubject() + " Body: " + messages.getBody() + " Tags: "
				+ messages.getTags() + " Priority: " + messages.getPriority() + "\r\n";
		data += "\r\n\r\n";

		response.setContentType("text/plain");
		response.setHeader("Content-Disposition", "attachment;filename=data_user_account.txt");
		final ServletOutputStream out = response.getOutputStream();
		out.println(data);
		out.flush();
		out.close();
	}
}
