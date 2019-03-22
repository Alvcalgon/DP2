
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
import services.HistoryService;
import services.MemberService;
import services.MessageService;
import services.ParadeService;
import services.SocialProfileService;
import controllers.AbstractController;
import domain.Box;
import domain.Brotherhood;
import domain.Float;
import domain.History;
import domain.InceptionRecord;
import domain.LegalRecord;
import domain.LinkRecord;
import domain.Member;
import domain.Message;
import domain.MiscellaneousRecord;
import domain.Parade;
import domain.PeriodRecord;
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
	@Autowired
	private HistoryService			historyService;
	@Autowired
	private MemberService			memberService;


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
		final History history = this.historyService.findHistoryByBrotherhood(brotherhood.getId());
		final InceptionRecord inceptionRecord = history.getInceptionRecord();
		final Collection<PeriodRecord> periodRecords = history.getPeriodRecords();
		final Collection<MiscellaneousRecord> miscellaneousRecords = history.getMiscellaneousRecords();
		final Collection<LegalRecord> legalRecords = history.getLegalRecords();
		final Collection<LinkRecord> linkRecords = history.getLinkRecords();
		final Collection<Member> members = this.memberService.findEnroledMemberByBrotherhood(brotherhood.getId());

		data += "\r\n\r\n";

		data += "Name: " + brotherhood.getName() + " Middle Name: " + brotherhood.getMiddleName() + " Surname: " + brotherhood.getSurname() + " Photo: " + brotherhood.getPhoto() + " Email: " + brotherhood.getEmail() + " Phone Number: "
			+ brotherhood.getPhoneNumber() + " Address " + brotherhood.getAddress() + " \r\n";
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
		data += "Floats:\r\n\r\n";
		for (final Float flooat : floats)
			data += "Title: " + flooat.getTitle() + " Description: " + flooat.getDescription() + " Pictures: " + flooat.getPictures() + "\r\n";
		data += "\r\n\r\n";
		data += "Processions:\r\n\r\n";
		for (final Parade parade : parades)
			data += "Parade title: " + parade.getTitle() + " Description: " + parade.getDescription() + " Moment: " + parade.getMoment() + " Final Mode: " + parade.getIsFinalMode() + " Status: " + parade.getStatus() + " Reason Why: "
				+ parade.getReasonWhy() + "\r\n";
		data += "\r\n\r\n";
		data += "History:\r\n\r\n";
		data += "Inception Record:\r\n\r\n";
		data += "Title: " + inceptionRecord.getTitle() + " Text: " + inceptionRecord.getText() + " Photos: " + inceptionRecord.getPhotos() + "\r\n";
		data += "Period Record:\r\n\r\n";
		for (final PeriodRecord periodRecord : periodRecords)
			data += "Title: " + periodRecord.getTitle() + " Text: " + periodRecord.getText() + " Photos: " + periodRecord.getPhotos() + " End Year: " + periodRecord.getEndYear() + " Start Year: " + periodRecord.getStartYear() + "\r\n";
		data += "Miscellaneous Record:\r\n\r\n";
		for (final MiscellaneousRecord miscellaneousRecord : miscellaneousRecords)
			data += "Title: " + miscellaneousRecord.getTitle() + " Text: " + miscellaneousRecord.getText() + "\r\n";
		data += "Legal Record:\r\n\r\n";
		for (final LegalRecord legalRecord : legalRecords)
			data += "Title: " + legalRecord.getTitle() + " Text: " + legalRecord.getText() + " Name: " + legalRecord.getName() + " Vat Number: " + legalRecord.getVatNumber() + " Laws: " + legalRecord.getLaws() + "\r\n";
		data += "Link Record:\r\n\r\n";
		for (final LinkRecord linkRecord : linkRecords)
			data += "Title: " + linkRecord.getTitle() + " Text: " + linkRecord.getText() + "\r\n";
		data += "\r\n\r\n";
		data += "Members:\r\n\r\n";
		for (final Member member : members)
			data += "Full Name: " + member.getFullname() + " Email: " + member.getEmail() + " Telephone Number: " + member.getPhoneNumber() + "\r\n";
		data += "\r\n\r\n";

		response.setContentType("text/plain");
		response.setHeader("Content-Disposition", "attachment;filename=data_user_account.txt");
		final ServletOutputStream out = response.getOutputStream();
		out.println(data);
		out.flush();
		out.close();
	}

}
