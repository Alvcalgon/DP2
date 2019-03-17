
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.LegalRecordRepository;
import domain.History;
import domain.LegalRecord;

@Service
@Transactional
public class LegalRecordService {

	// Managed repository ------------------------------
	@Autowired
	private LegalRecordRepository	legalRecordRepository;

	// Supporting services -----------------------------
	@Autowired
	private HistoryService			historyService;


	// Constructors ------------------------------------

	public LegalRecordService() {
		super();
	}
	// Simple CRUD methods -----------------------------

	public LegalRecord create() {
		LegalRecord result;

		result = new LegalRecord();

		return result;
	}

	public LegalRecord findOne(final int legalRecordId) {
		Assert.isTrue(legalRecordId != 0);
		LegalRecord result;

		result = this.legalRecordRepository.findOne(legalRecordId);
		Assert.notNull(result);

		return result;

	}

	public LegalRecord findOneEdit(final int legalRecordId) {
		Assert.isTrue(legalRecordId != 0);
		LegalRecord result;

		result = this.legalRecordRepository.findOne(legalRecordId);

		this.checkByPrincipal(result);
		Assert.notNull(result);

		return result;

	}

	protected Collection<LegalRecord> findAll() {
		Collection<LegalRecord> results;

		results = this.legalRecordRepository.findAll();
		Assert.notNull(results);

		return results;
	}

	public LegalRecord save(final LegalRecord legalRecord) {
		Assert.notNull(legalRecord);

		LegalRecord result;

		result = this.legalRecordRepository.save(legalRecord);

		if (this.legalRecordRepository.exists(legalRecord.getId()))
			this.checkByPrincipal(legalRecord);
		else {
			History history;

			history = this.historyService.findByPrincipal();

			this.historyService.addLegalRecord(history, result);
		}

		return result;
	}

	public void delete(final LegalRecord legalRecord) {
		Assert.notNull(legalRecord);
		Assert.isTrue(legalRecord.getId() != 0);
		this.checkByPrincipal(legalRecord);

		History history;

		history = this.historyService.findByPrincipal();

		this.historyService.removeLegalRecord(history, legalRecord);
		this.legalRecordRepository.delete(legalRecord);
	}

	// Other business methods --------------------------
	private void checkByPrincipal(final LegalRecord legalRecord) {
		History history;

		history = this.historyService.findByPrincipal();

		Assert.isTrue(history.getLegalRecords().contains(legalRecord));
	}
	protected void flush() {
		this.legalRecordRepository.flush();
	}
}
