
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
import domain.Brotherhood;
import domain.History;
import domain.MiscellaneousRecord;

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


	// Suite test -----------------------------------

	/**
	 * A: Requirement tested: level C: requirement 4.1: The average, the minimum, the maximum
	 * and the standard deviation of the records per history.
	 * C: 100% se ha recorrido todas las líneas de código del método
	 * D:
	 */
	@Test
	public void dataNumberRecordsPerHistory() {
		Double[] data;

		data = this.historyService.findDataNumberRecordsPerHistory();

		Assert.notNull(data);
		Assert.isTrue(data.length == 4);
	}

	/**
	 * A: Requirement tested: level C: requirement 4.1: The brotherhood with the largest history.
	 * C: 100% se ha recorrido todas las líneas de código del método
	 * D:
	 */
	@Test
	public void brotherhoohLargestHistory() {
		Collection<Brotherhood> brotherhood;

		brotherhood = this.historyService.findBrotherhoohLargestHistory();

		Assert.notNull(brotherhood);
		Assert.isTrue(brotherhood.size() == 1);
	}

	/**
	 * A: Requirement tested: level C: requirement 4.1: The brotherhood with the largest history.
	 * C: 100% se ha recorrido todas las líneas de código del método
	 * D:
	 */
	@Test
	public void brotherhoohsLargestHistoryAvg_positive_test() {
		Collection<Brotherhood> brotherhood;
		History history;
		final Collection<MiscellaneousRecord> miscellaneousRecords;
		final MiscellaneousRecord miscellaneousRecord;

		brotherhood = this.historyService.findBrotherhoohsLargestHistoryAvg();
		history = this.historyService.findOne(super.getEntityId("history1"));
		miscellaneousRecord = this.miscellaneousRecordService.findOne(super.getEntityId("miscellaneousRecord1"));
		miscellaneousRecords = history.getMiscellaneousRecords();
		miscellaneousRecords.add(miscellaneousRecord);
		miscellaneousRecords.add(miscellaneousRecord);
		miscellaneousRecords.add(miscellaneousRecord);
		miscellaneousRecords.add(miscellaneousRecord);
		miscellaneousRecords.add(miscellaneousRecord);
		miscellaneousRecords.add(miscellaneousRecord);
		miscellaneousRecords.add(miscellaneousRecord);
		miscellaneousRecords.add(miscellaneousRecord);
		miscellaneousRecords.add(miscellaneousRecord);
		miscellaneousRecords.add(miscellaneousRecord);
		miscellaneousRecords.add(miscellaneousRecord);
		miscellaneousRecords.add(miscellaneousRecord);
		miscellaneousRecords.add(miscellaneousRecord);

		Assert.isTrue(brotherhood.contains(history.getBrotherhood()));
	}

}
