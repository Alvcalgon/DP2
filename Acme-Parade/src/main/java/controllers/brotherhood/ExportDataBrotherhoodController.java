
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
import domain.Actor;
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

		final Brotherhood actor = this.brotherhoodService.findByPrincipal();
		final Collection<SocialProfile> socialProfiles = this.socialProfileService.findSocialProfilesByActor(actor.getId());
		final Collection<Message> messagesSent = this.messageService.findSentMessagesByActor(actor.getId());
		final Collection<Message> messagesReceived = this.messageService.findReceivedMessagesByActor(actor.getId());
		final Collection<Box> boxs = this.boxService.findBoxesByActor(actor.getId());
		final Collection<Float> floats = this.floatService.findFloatByBrotherhood(actor.getId());
		final Collection<Parade> parades = this.paradeService.findParadeByBrotherhood(actor.getId());
		final History history = this.historyService.findHistoryByBrotherhood(actor.getId());
		final InceptionRecord inceptionRecord = history.getInceptionRecord();
		final Collection<PeriodRecord> periodRecords = history.getPeriodRecords();
		final Collection<MiscellaneousRecord> miscellaneousRecords = history.getMiscellaneousRecords();
		final Collection<LegalRecord> legalRecords = history.getLegalRecords();
		final Collection<LinkRecord> linkRecords = history.getLinkRecords();
		final Collection<Member> members = this.memberService.findEnroledMemberByBrotherhood(actor.getId());

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

		data += "Floats:\r\n\r\n";
		Integer f = 0;
		for (final Float flooat : floats) {
			data += "Title: " + flooat.getTitle() + " \r\n" + "Description: " + flooat.getDescription() + " \r\n" + "Pictures: " + flooat.getPictures() + "\r\n";
			f++;
			if (f < floats.size())
				data += "\r\n" + "......................." + "\r\n\r\n";
		}

		data += "\r\n";
		data += "-------------------------------------------------------------";
		data += "\r\n\r\n";

		data += "Processions:\r\n\r\n";
		Integer p = 0;
		for (final Parade parade : parades) {
			data += "Parade title: " + parade.getTitle() + " \r\n" + "Description: " + parade.getDescription() + " \r\n" + "Moment: " + parade.getMoment() + " \r\n" + "Final Mode: " + parade.getIsFinalMode() + " \r\n" + "Status: " + parade.getStatus()
				+ " \r\n" + "Reason Why: " + parade.getReasonWhy() + "\r\n";
			p++;
			if (p < parades.size())
				data += "\r\n" + "......................." + "\r\n\r\n";
		}

		data += "\r\n";
		data += "-------------------------------------------------------------";
		data += "\r\n\r\n";

		data += "History:\r\n\r\n";

		data += "Inception Record:\r\n\r\n";

		data += "Title: " + inceptionRecord.getTitle() + " \r\n" + "Text: " + inceptionRecord.getText() + " \r\n" + "Photos: " + inceptionRecord.getPhotos() + "\r\n";

		data += "\r\n";
		data += "- - - -";
		data += "\r\n\r\n";

		data += "Period Record:\r\n\r\n";
		Integer pr = 0;
		for (final PeriodRecord periodRecord : periodRecords) {
			data += "Title: " + periodRecord.getTitle() + " \r\n" + "Text: " + periodRecord.getText() + " \r\n" + "Photos: " + periodRecord.getPhotos() + " \r\n" + "End Year: " + periodRecord.getEndYear() + " \r\n" + "Start Year: "
				+ periodRecord.getStartYear() + "\r\n";
			pr++;
			if (pr < periodRecords.size())
				data += "\r\n" + "......................." + "\r\n\r\n";
		}

		data += "\r\n";
		data += "- - - -";
		data += "\r\n\r\n";

		data += "Miscellaneous Record:\r\n\r\n";
		Integer mr = 0;
		for (final MiscellaneousRecord miscellaneousRecord : miscellaneousRecords) {
			data += "Title: " + miscellaneousRecord.getTitle() + " \r\n" + "Text: " + miscellaneousRecord.getText() + "\r\n";
			mr++;
			if (mr < miscellaneousRecords.size())
				data += "\r\n" + "......................." + "\r\n\r\n";
		}

		data += "\r\n";
		data += "- - - -";
		data += "\r\n\r\n";

		data += "Legal Record:\r\n\r\n";
		Integer lr = 0;
		for (final LegalRecord legalRecord : legalRecords) {
			data += "Title: " + legalRecord.getTitle() + " \r\n" + "Text: " + legalRecord.getText() + " \r\n" + "Name: " + legalRecord.getName() + " \r\n" + "Vat Number: " + legalRecord.getVatNumber() + " \r\n" + "Laws: " + legalRecord.getLaws() + "\r\n";
			lr++;
			if (lr < legalRecords.size())
				data += "\r\n" + "......................." + "\r\n\r\n";
		}
		data += "\r\n";
		data += "- - - -";
		data += "\r\n\r\n";

		data += "Link Record:\r\n\r\n";
		Integer lir = 0;
		for (final LinkRecord linkRecord : linkRecords) {
			data += "Title: " + linkRecord.getTitle() + " \r\n" + "Text: " + linkRecord.getText() + "\r\n";
			lir++;
			if (lir < linkRecords.size())
				data += "\r\n" + "......................." + "\r\n\r\n";
		}

		data += "\r\n";
		data += "-------------------------------------------------------------";
		data += "\r\n\r\n";

		data += "Members:\r\n\r\n";
		Integer mem = 0;
		for (final Member member : members) {
			data += "Full Name: " + member.getFullname() + " \r\n" + "Email: " + member.getEmail() + " \r\n" + "Telephone Number: " + member.getPhoneNumber() + "\r\n";
			mem++;
			if (mem < members.size())
				data += "\r\n" + "......................." + "\r\n\r\n";
		}

		data += "\r\n";

		response.setContentType("text/plain");
		response.setHeader("Content-Disposition", "attachment;filename=data_user_account.txt");
		final ServletOutputStream out = response.getOutputStream();
		out.println(data);
		out.flush();
		out.close();
	}
}
