
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
		String data = "Data of your user account:\r\n";

		final Chapter chapter = this.chapterService.findByPrincipal();
		final Collection<SocialProfile> socialProfiles = this.socialProfileService.findSocialProfilesByActor(chapter.getId());
		final Collection<Message> messagesSent = this.messageService.findSentMessagesByActor(chapter.getId());
		final Collection<Message> messagesReceived = this.messageService.findReceivedMessagesByActor(chapter.getId());
		final Collection<Box> boxs = this.boxService.findBoxesByActor(chapter.getId());
		final Collection<Proclaim> proclaims = this.proclaimService.findByChapterId(chapter.getId());

		data += "\r\n\r\n";

		data += "Name: " + chapter.getName() + " Middle Name: " + chapter.getMiddleName() + " Surname: " + chapter.getSurname() + " Title: " + chapter.getTitle() + " Photo: " + chapter.getPhoto() + " Email: " + chapter.getEmail() + " Phone Number: "
			+ chapter.getPhoneNumber() + " Address " + chapter.getAddress() + " \r\n";
		data += "\r\n\r\n";
		data += "Area:\r\n";
		data += "Name: " + chapter.getArea() + " \r\n";
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
		data += "Proclaims:\r\n\r\n";
		for (final Proclaim proclaim : proclaims)
			data += "Published Moment: " + proclaim.getPublishedMoment() + " Test: " + proclaim.getText() + "\r\n";
		data += "\r\n\r\n";

		response.setContentType("text/plain");
		response.setHeader("Content-Disposition", "attachment;filename=data_user_account.txt");
		final ServletOutputStream out = response.getOutputStream();
		out.println(data);
		out.flush();
		out.close();
	}
}
