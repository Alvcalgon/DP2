
package services;

import java.util.Calendar;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.PeriodRecordRepository;
import domain.History;
import domain.PeriodRecord;

@Service
@Transactional
public class PeriodRecordService {

	// Managed repository ------------------------------
	@Autowired
	private PeriodRecordRepository	periodRecordRepository;

	// Supporting services -----------------------------
	@Autowired
	private HistoryService			historyService;

	@Autowired
	private UtilityService			utilityService;


	// Constructors ------------------------------------

	public PeriodRecordService() {
		super();
	}
	// Simple CRUD methods -----------------------------

	public PeriodRecord create() {
		PeriodRecord result;
		Assert.isTrue(!(this.historyService.findByPrincipal() == null));

		result = new PeriodRecord();

		return result;
	}

	public PeriodRecord findOne(final int periodRecordId) {
		Assert.isTrue(periodRecordId != 0);
		PeriodRecord result;

		result = this.periodRecordRepository.findOne(periodRecordId);
		Assert.notNull(result);

		return result;

	}

	public PeriodRecord findOneEdit(final int periodRecordId) {
		Assert.isTrue(periodRecordId != 0);
		PeriodRecord result;

		result = this.periodRecordRepository.findOne(periodRecordId);

		this.checkByPrincipal(result);
		Assert.notNull(result);

		return result;

	}

	protected Collection<PeriodRecord> findAll() {
		Collection<PeriodRecord> results;

		results = this.periodRecordRepository.findAll();
		Assert.notNull(results);

		return results;
	}

	public PeriodRecord save(final PeriodRecord periodRecord) {
		Assert.notNull(periodRecord);
		this.checkYearsPeriod(periodRecord);
		//Assert.isTrue(periodRecord.getStartYear() <= periodRecord.getEndYear());
		this.utilityService.checkPicture(periodRecord.getPhotos());

		PeriodRecord result;

		if (periodRecord.getId() == 0) {
			History history;
			history = this.historyService.findByPrincipal();
			result = this.periodRecordRepository.save(periodRecord);
			this.historyService.addPeriodRecord(history, result);
		} else {
			this.checkByPrincipal(periodRecord);
			result = this.periodRecordRepository.save(periodRecord);
		}

		return result;
	}

	public void delete(final PeriodRecord periodRecord) {
		Assert.notNull(periodRecord);
		Assert.isTrue(periodRecord.getId() != 0);
		this.checkByPrincipal(periodRecord);

		History history;

		history = this.historyService.findByPrincipal();

		this.historyService.removePeriodRecord(history, periodRecord);
		this.periodRecordRepository.delete(periodRecord);
	}

	// Other business methods --------------------------
	private void checkByPrincipal(final PeriodRecord periodRecord) {
		History history;

		history = this.historyService.findByPrincipal();

		Assert.isTrue(history.getPeriodRecords().contains(periodRecord));
	}

	private void checkYearsPeriod(final PeriodRecord periodRecord) {
		final Calendar cal = Calendar.getInstance();
		final int year = cal.get(Calendar.YEAR);
		if ((!(periodRecord.getStartYear() <= periodRecord.getEndYear())) || (periodRecord.getStartYear() > year) || (periodRecord.getEndYear() > year))
			throw new DataIntegrityViolationException("Invalid yearPeriod");
	}

	protected void flush() {
		this.periodRecordRepository.flush();
	}
}
