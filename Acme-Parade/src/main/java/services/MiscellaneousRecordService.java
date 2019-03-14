
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.MiscellaneousRecordRepository;
import domain.History;
import domain.MiscellaneousRecord;

@Service
@Transactional
public class MiscellaneousRecordService {

	// Managed repository ------------------------------
	@Autowired
	private MiscellaneousRecordRepository	miscellaneousRecordRepository;

	// Supporting services -----------------------------
	@Autowired
	private HistoryService					historyService;


	// Constructors ------------------------------------

	public MiscellaneousRecordService() {
		super();
	}
	// Simple CRUD methods -----------------------------

	public MiscellaneousRecord create() {
		MiscellaneousRecord result;

		result = new MiscellaneousRecord();

		return result;
	}

	public MiscellaneousRecord findOne(final int miscellaneousRecordId) {
		Assert.isTrue(miscellaneousRecordId != 0);
		MiscellaneousRecord result;

		result = this.miscellaneousRecordRepository.findOne(miscellaneousRecordId);
		Assert.notNull(result);

		return result;

	}

	public MiscellaneousRecord findOneEdit(final int miscellaneousRecordId) {
		Assert.isTrue(miscellaneousRecordId != 0);
		MiscellaneousRecord result;

		result = this.miscellaneousRecordRepository.findOne(miscellaneousRecordId);

		this.checkByPrincipal(result);
		Assert.notNull(result);

		return result;
	}

	protected Collection<MiscellaneousRecord> findAll() {
		Collection<MiscellaneousRecord> results;

		results = this.miscellaneousRecordRepository.findAll();
		Assert.notNull(results);

		return results;
	}

	public MiscellaneousRecord save(final MiscellaneousRecord miscellaneousRecord) {
		Assert.notNull(miscellaneousRecord);

		MiscellaneousRecord result;

		result = this.miscellaneousRecordRepository.save(miscellaneousRecord);

		if (this.miscellaneousRecordRepository.exists(miscellaneousRecord.getId()))
			this.checkByPrincipal(miscellaneousRecord);
		else {
			History history;

			history = this.historyService.findByPrincipal();

			this.historyService.addMiscellaneousRecord(history, result);
		}

		return result;
	}

	public void delete(final MiscellaneousRecord miscellaneousRecord) {
		Assert.notNull(miscellaneousRecord);
		Assert.isTrue(miscellaneousRecord.getId() != 0);
		this.checkByPrincipal(miscellaneousRecord);

		History history;

		history = this.historyService.findByPrincipal();

		this.historyService.removeMiscellaneousRecord(history, miscellaneousRecord);
		this.miscellaneousRecordRepository.delete(miscellaneousRecord);
	}

	// Other business methods --------------------------
	private void checkByPrincipal(final MiscellaneousRecord miscellaneousRecord) {
		History history;

		history = this.historyService.findByPrincipal();

		Assert.isTrue(history.getMiscellaneousRecords().contains(miscellaneousRecord));
	}
}
