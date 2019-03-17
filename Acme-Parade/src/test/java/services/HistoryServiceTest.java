
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.History;
import domain.InceptionRecord;
import domain.LegalRecord;
import domain.LinkRecord;
import domain.MiscellaneousRecord;
import domain.PeriodRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class HistoryServiceTest extends AbstractTest {

	// Services under test --------------------------
	@Autowired
	private HistoryService				historyService;

	// Other supporting services --------------------
	@Autowired
	private MiscellaneousRecordService	miscellaneousRecordService;

	@Autowired
	private LinkRecordService			linkRecordService;

	@Autowired
	private LegalRecordService			legalRecordService;

	@Autowired
	private InceptionRecordService		inceptionRecordService;

	@Autowired
	private PeriodRecordService			periodRecordService;

	@Autowired
	private BrotherhoodService			brotherhoodService;


	// Suite test -----------------------------------

	@Test
	public void driverCreate() {
		final Object testingData[][] = {
			/*
			 * A: Req1, 3.1
			 * B: Crea adecuadamente una historia a traves de crear una inceptionRecord
			 * C:
			 * D:
			 */
			{
				"brotherhood4", null, "inceptionRecordTest1", "textInceptionRecordTest1", "http://photo.com", null, null, null, null
			},
			/*
			 * A: Req1, 3.1
			 * B: Crea adecuadamente un linkRecord
			 * C:
			 * D:
			 */
			{
				"brotherhood4", "history4", "inceptionRecordTest1", "textInceptionRecordTest1", "http://photo.com", "inceptionRecordTest1", "textInceptionRecordTest1", "http://photo.com", null
			},

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCreate((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);

	}

	protected void templateCreate(final String username, final Integer historyId, final String titleInception, final String textInception, final String photoInception, final Class<?> expected) {
		Class<?> caught;
		final History history;
		InceptionRecord inceptionRecord;
		InceptionRecord inceptionRecordSaved;
		Collection<LinkRecord> linkRecords;
		Collection<LegalRecord> legalRecords;
		Collection<MiscellaneousRecord> miscellaneousRecords;
		Collection<PeriodRecord> periodRecords;

		this.startTransaction();

		caught = null;
		try {
			super.authenticate(username);

			if (historyId == null)
				inceptionRecord = this.inceptionRecordService.create();
			else {
				history = this.historyService.findOne(historyId);
				inceptionRecord = history.getInceptionRecord();
				linkRecords = history.getLinkRecords();
				legalRecords = history.getLegalRecords();
				miscellaneousRecords = history.getMiscellaneousRecords();
				periodRecords = history.getPeriodRecords();
			}

			inceptionRecord.setTitle(titleInception);
			inceptionRecord.setText(textInception);
			inceptionRecord.setPhotos(photoInception);

			inceptionRecordSaved = this.inceptionRecordService.save(inceptionRecord);
			this.inceptionRecordService.flush();

			Assert.notNull(inceptionRecordSaved);
			Assert.isTrue(inceptionRecordSaved.getId() != 0);

			super.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.rollbackTransaction();

		super.checkExceptions(expected, caught);
	}

	//	/*
	//	 * A: Requirement tested: level C: requirement 2: An actor who is not authenticated must be able to
	//	 * display the history of every brotherhood that he or she can display.
	//	 * C: 100% se ha recorrido todas las líneas de código del método
	//	 * D:
	//	 */
	//	@Test
	//	public void displayHistory_positive_test() {
	//		History history;
	//
	//		history = this.historyService.findOne(super.getEntityId("history1"));
	//
	//		Assert.notNull(history);
	//
	//	}
	//
	//	/*
	//	 * A: Requirement tested: level C: requirement 2: An actor who is not authenticated must be able to
	//	 * display the history of every brotherhood that he or she can display.
	//	 * C: 100% se ha recorrido todas las líneas de código del método
	//	 * D:
	//	 */
	//	@Test
	//	public void displayMiscelleneousRecord_positive_test() {
	//		MiscellaneousRecord miscellaneousRecord;
	//
	//		miscellaneousRecord = this.miscellaneousRecordService.findOne(super.getEntityId("miscellaneousRecord1"));
	//
	//		Assert.notNull(miscellaneousRecord);
	//
	//	}
	//
	//	/*
	//	 * A: Requirement tested: level C: requirement 2: An actor who is not authenticated must be able to
	//	 * display the history of every brotherhood that he or she can display.
	//	 * C: 100% se ha recorrido todas las líneas de código del método
	//	 * D:
	//	 */
	//	@Test
	//	public void displayLinkRecord_positive_test() {
	//		LinkRecord linkRecord;
	//
	//		linkRecord = this.linkRecordService.findOne(super.getEntityId("linkRecord1"));
	//
	//		Assert.notNull(linkRecord);
	//
	//	}
	//
	//	/*
	//	 * A: Requirement tested: level C: requirement 2: An actor who is not authenticated must be able to
	//	 * display the history of every brotherhood that he or she can display.
	//	 * C: 100% se ha recorrido todas las líneas de código del método
	//	 * D:
	//	 */
	//	@Test
	//	public void displayLegalRecord_positive_test() {
	//		LegalRecord legalRecord;
	//
	//		legalRecord = this.legalRecordService.findOne(super.getEntityId("legalRecord1"));
	//
	//		Assert.notNull(legalRecord);
	//
	//	}
	//
	//	/*
	//	 * A: Requirement tested: level C: requirement 2: An actor who is not authenticated must be able to
	//	 * display the history of every brotherhood that he or she can display.
	//	 * C: 100% se ha recorrido todas las líneas de código del método
	//	 * D:
	//	 */
	//	@Test
	//	public void displayInceptionRecord_positive_test() {
	//		InceptionRecord inceptionRecord;
	//
	//		inceptionRecord = this.inceptionRecordService.findOne(super.getEntityId("inceptionRecord1"));
	//
	//		Assert.notNull(inceptionRecord);
	//
	//	}
	//
	//	/*
	//	 * A: Requirement tested: level C: requirement 2: An actor who is not authenticated must be able to
	//	 * display the history of every brotherhood that he or she can display.
	//	 * C: 100% se ha recorrido todas las líneas de código del método
	//	 * D:
	//	 */
	//	@Test
	//	public void displayPeriodRecord_positive_test() {
	//		PeriodRecord periodRecord;
	//
	//		periodRecord = this.periodRecordService.findOne(super.getEntityId("periodRecord1"));
	//
	//		Assert.notNull(periodRecord);
	//
	//	}
	//
	//	/*
	//	 * A: Requirement tested: level C: requirement 4.1: The average, the minimum, the maximum
	//	 * and the standard deviation of the records per history.
	//	 * C: 100% se ha recorrido todas las líneas de código del método
	//	 * D:
	//	 */
	//	@Test
	//	public void dataNumberRecordsPerHistory() {
	//		Double[] data;
	//
	//		data = this.historyService.findDataNumberRecordsPerHistory();
	//
	//		Assert.notNull(data);
	//		Assert.isTrue(data.length == 4);
	//	}
	//
	//	/*
	//	 * A: Requirement tested: level C: requirement 4.1: The brotherhood with the largest history.
	//	 * C: 100% se ha recorrido todas las líneas de código del método
	//	 * D:
	//	 */
	//	@Test
	//	public void brotherhoohLargestHistory() {
	//		Collection<Brotherhood> brotherhood;
	//
	//		brotherhood = this.historyService.findBrotherhoohLargestHistory();
	//
	//		Assert.notNull(brotherhood);
	//		Assert.isTrue(brotherhood.size() == 1);
	//	}
	//
	//	/*
	//	 * A: Requirement tested: level C: requirement 4.1: The brotherhood with the largest history.
	//	 * C: 100% se ha recorrido todas las líneas de código del método
	//	 * D:
	//	 */
	//	@Test
	//	public void brotherhoohsLargestHistoryAvg_positive_test() {
	//		Collection<Brotherhood> brotherhood;
	//		History history;
	//		final Collection<MiscellaneousRecord> miscellaneousRecords;
	//		final MiscellaneousRecord miscellaneousRecord;
	//
	//		brotherhood = this.historyService.findBrotherhoohsLargestHistoryAvg();
	//		history = this.historyService.findOne(super.getEntityId("history1"));
	//		miscellaneousRecord = this.miscellaneousRecordService.findOne(super.getEntityId("miscellaneousRecord1"));
	//		miscellaneousRecords = history.getMiscellaneousRecords();
	//		miscellaneousRecords.add(miscellaneousRecord);
	//		miscellaneousRecords.add(miscellaneousRecord);
	//		miscellaneousRecords.add(miscellaneousRecord);
	//		miscellaneousRecords.add(miscellaneousRecord);
	//		miscellaneousRecords.add(miscellaneousRecord);
	//		miscellaneousRecords.add(miscellaneousRecord);
	//		miscellaneousRecords.add(miscellaneousRecord);
	//		miscellaneousRecords.add(miscellaneousRecord);
	//		miscellaneousRecords.add(miscellaneousRecord);
	//		miscellaneousRecords.add(miscellaneousRecord);
	//		miscellaneousRecords.add(miscellaneousRecord);
	//		miscellaneousRecords.add(miscellaneousRecord);
	//		miscellaneousRecords.add(miscellaneousRecord);
	//
	//		Assert.isTrue(brotherhood.contains(history.getBrotherhood()));
	//	}
	//
	//	/*
	//	 * A: Requirement tested: level C: requirement 4.1: The brotherhood with the largest history.
	//	 * B:
	//	 * C: 100% se ha recorrido todas las líneas de código del método
	//	 * D:
	//	 */
	//	@Test(expected = IllegalArgumentException.class)
	//	public void brotherhoohsLargestHistoryAvg_negative_test() {
	//		Collection<Brotherhood> brotherhood;
	//		History history;
	//		final Collection<MiscellaneousRecord> miscellaneousRecords;
	//		final MiscellaneousRecord miscellaneousRecord;
	//
	//		brotherhood = this.historyService.findBrotherhoohsLargestHistoryAvg();
	//		history = this.historyService.findOne(super.getEntityId("history1"));
	//		miscellaneousRecord = this.miscellaneousRecordService.findOne(super.getEntityId("miscellaneousRecord1"));
	//		miscellaneousRecords = history.getMiscellaneousRecords();
	//		miscellaneousRecords.add(miscellaneousRecord);
	//		miscellaneousRecords.add(miscellaneousRecord);
	//		miscellaneousRecords.add(miscellaneousRecord);
	//		miscellaneousRecords.add(miscellaneousRecord);
	//		miscellaneousRecords.add(miscellaneousRecord);
	//		miscellaneousRecords.add(miscellaneousRecord);
	//		miscellaneousRecords.add(miscellaneousRecord);
	//		miscellaneousRecords.add(miscellaneousRecord);
	//		miscellaneousRecords.add(miscellaneousRecord);
	//		miscellaneousRecords.add(miscellaneousRecord);
	//		miscellaneousRecords.add(miscellaneousRecord);
	//		miscellaneousRecords.add(miscellaneousRecord);
	//		miscellaneousRecords.add(miscellaneousRecord);
	//
	//		Brotherhood brotherhood2;
	//		brotherhood2 = this.brotherhoodService.findOne(super.getEntityId("brotherhood2"));
	//
	//		Assert.isTrue(brotherhood.contains(brotherhood2));
	//	}
}
