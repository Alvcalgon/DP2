
package controllers.sponsor;

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
import services.MessageService;
import services.SocialProfileService;
import services.SponsorService;
import services.SponsorshipService;
import controllers.AbstractController;
import domain.Box;
import domain.Message;
import domain.SocialProfile;
import domain.Sponsor;
import domain.Sponsorship;

@Controller
@RequestMapping(value = "/exportData/sponsor")
public class ExportDataSponsorController extends AbstractController {

	@Autowired
	private SocialProfileService	socialProfileService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private BoxService				boxService;

	@Autowired
	private SponsorshipService		sponsorshipService;

	@Autowired
	private SponsorService			sponsorService;


	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public void test(final HttpSession session, final HttpServletResponse response) throws IOException {
		String data = "Data of your user account:\r\n";

		final Sponsor sponsor = this.sponsorService.findByPrincipal();
		final Collection<SocialProfile> socialProfiles = this.socialProfileService.findSocialProfilesByActor(sponsor.getId());
		final Collection<Message> messagesSent = this.messageService.findSentMessagesByActor(sponsor.getId());
		final Collection<Message> messagesReceived = this.messageService.findReceivedMessagesByActor(sponsor.getId());
		final Collection<Box> boxs = this.boxService.findBoxesByActor(sponsor.getId());
		final Collection<Sponsorship> sponsorships = this.sponsorshipService.findAllByPrincipal();

		data += "\r\n\r\n";

		data += "Name: " + sponsor.getName() + " Middle Name: " + sponsor.getMiddleName() + " Surname: " + sponsor.getSurname() + " Photo: " + sponsor.getPhoto() + " Email: " + sponsor.getEmail() + " Phone Number: " + sponsor.getPhoneNumber()
			+ " Address " + sponsor.getAddress() + " \r\n";
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
		data += "Sponsorship:\r\n\r\n";
		for (final Sponsorship sponsorship : sponsorships)
			data += "Banner: " + sponsorship.getBanner() + " Target URL: " + sponsorship.getTargetURL() + " Credit Card: " + sponsorship.getCreditCard().getNumber() + " Is Active : " + sponsorship.getIsActive() + "\r\n";
		data += "\r\n\r\n";

		response.setContentType("text/plain");
		response.setHeader("Content-Disposition", "attachment;filename=data_user_account.txt");
		final ServletOutputStream out = response.getOutputStream();
		out.println(data);
		out.flush();
		out.close();
	}
}
