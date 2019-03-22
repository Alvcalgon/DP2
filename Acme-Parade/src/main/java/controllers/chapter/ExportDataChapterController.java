
package controllers.chapter;

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
import services.ChapterService;
import services.MessageService;
import services.ProclaimService;
import services.SocialProfileService;
import controllers.AbstractController;
import domain.Actor;
import domain.Box;
import domain.Chapter;
import domain.Message;
import domain.Proclaim;
import domain.SocialProfile;

@Controller
@RequestMapping(value = "/exportData/chapter")
public class ExportDataChapterController extends AbstractController {

	@Autowired
	private SocialProfileService	socialProfileService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private BoxService				boxService;

	@Autowired
	private ChapterService			chapterService;

	@Autowired
	private ProclaimService			proclaimService;


	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public void test(final HttpSession session, final HttpServletResponse response) throws IOException {

		final Chapter actor = this.chapterService.findByPrincipal();
		final Collection<SocialProfile> socialProfiles = this.socialProfileService.findSocialProfilesByActor(actor.getId());
		final Collection<Message> messagesSent = this.messageService.findSentMessagesByActor(actor.getId());
		final Collection<Message> messagesReceived = this.messageService.findReceivedMessagesByActor(actor.getId());
		final Collection<Box> boxs = this.boxService.findBoxesByActor(actor.getId());
		final Collection<Proclaim> proclaims = this.proclaimService.findByChapterId(actor.getId());

		String data = "Data of your user account:\r\n";
		data += "\r\n";

		data += "Name: " + actor.getName() + " \r\n" + "Middle Name: " + actor.getMiddleName() + " \r\n" + "Surname: " + actor.getSurname() + " \r\n" + "Photo: " + actor.getPhoto() + " \r\n" + "Email: " + actor.getEmail() + " \r\n" + "Phone Number: "
			+ actor.getPhoneNumber() + " \r\n" + "Address: " + actor.getAddress() + " \r\n";

		data += "\r\n\r\n";
		data += "-------------------------------------------------------------";
		data += "\r\n\r\n";

		data += "Social Profiles:\r\n";
		data += "\r\n";

		for (final SocialProfile socialProfile : socialProfiles)
			data += "Nick: " + socialProfile.getNick() + " \r\n" + "Link profile: " + socialProfile.getLinkProfile() + " \r\n" + "Social Network: " + socialProfile.getSocialNetwork() + "\r\n";

		data += "\r\n\r\n";
		data += "-------------------------------------------------------------";
		data += "\r\n\r\n";

		data += "Boxes:\r\n";
		data += "\r\n";

		for (final Box box : boxs)
			data += "Name: " + box.getName() + "\r\n";

		data += "\r\n\r\n";
		data += "-------------------------------------------------------------";
		data += "\r\n\r\n";

		data += "Sent Messages:\r\n\r\n";
		Integer m = 0;
		for (final Message message : messagesSent) {
			final Collection<Actor> recipients = message.getRecipients();
			data += "Sender: " + message.getSender().getFullname() + " \r\n";
			for (final Actor recipient : recipients)
				data += "Recipients: " + recipient.getFullname() + " \r\n";
			data += "Sent Moment: " + message.getSentMoment() + " \r\n" + "Subject: " + message.getSubject() + " \r\n" + "Body: " + message.getBody() + " \r\n" + "Tags: " + message.getTags() + " \r\n" + "Priority: " + message.getPriority() + " \r\n";
			m++;
			if (m < messagesSent.size())
				data += "\r\n" + "......................." + "\r\n\r\n";
		}

		data += "\r\n";
		data += "-------------------------------------------------------------";
		data += "\r\n\r\n";

		data += "Received Messages:\r\n\r\n";
		Integer n = 0;
		for (final Message message : messagesReceived) {
			final Collection<Actor> recipients = message.getRecipients();
			data += "Sender: " + message.getSender().getFullname() + " \r\n";
			for (final Actor recipient : recipients)
				data += "Recipients: " + recipient.getFullname() + " \r\n";
			data += "Sent Moment: " + message.getSentMoment() + " \r\n" + "Subject: " + message.getSubject() + " \r\n" + "Body: " + message.getBody() + " \r\n" + "Tags: " + message.getTags() + " \r\n" + "Priority: " + message.getPriority() + " \r\n";
			n++;
			if (n < messagesReceived.size())
				data += "\r\n" + "......................." + "\r\n\r\n";
		}

		data += "\r\n";
		data += "-------------------------------------------------------------";
		data += "\r\n\r\n";

		data += "Proclaims:\r\n\r\n";
		Integer pr = 0;
		for (final Proclaim proclaim : proclaims) {
			data += "Published Moment: " + proclaim.getPublishedMoment() + " \r\n" + "Test: " + proclaim.getText() + "\r\n";
			pr++;
			if (pr < proclaims.size())
				data += "\r\n" + "......................." + "\r\n\r\n";
		}

		data += "\r\n";
		data += "-------------------------------------------------------------";
		data += "\r\n\r\n";

		data += "Area:\r\n\r\n" + actor.getArea().getName();

		response.setContentType("text/plain");
		response.setHeader("Content-Disposition", "attachment;filename=data_user_account.txt");
		final ServletOutputStream out = response.getOutputStream();
		out.println(data);
		out.flush();
		out.close();
	}
}
