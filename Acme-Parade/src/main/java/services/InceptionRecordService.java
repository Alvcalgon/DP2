
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.InceptionRecordRepository;
import domain.History;
import domain.InceptionRecord;

@Service
@Transactional
public class InceptionRecordService {

	// Managed repository ------------------------------

	@Autowired
	private InceptionRecordRepository	inceptionRecordRepository;

	// Supporting services -----------------------------
	@Autowired
	private HistoryService				historyService;


	// Constructors ------------------------------------

	public InceptionRecordService() {
		super();
	}

	// Simple CRUD methods -----------------------------

	public InceptionRecord create() {
		InceptionRecord result;

		result = new InceptionRecord();

		return result;
	}

	public InceptionRecord findOne(final int inceptionRecordId) {
		Assert.isTrue(inceptionRecordId != 0);
		InceptionRecord result;

		result = this.inceptionRecordRepository.findOne(inceptionRecordId);
		Assert.notNull(result);

		return result;

	}

	protected Collection<InceptionRecord> findAll() {
		Collection<InceptionRecord> results;

		results = this.inceptionRecordRepository.findAll();
		Assert.notNull(results);

		return results;
	}

	public InceptionRecord save(final InceptionRecord inceptionRecord) {
		Assert.notNull(inceptionRecord);

		InceptionRecord result;

		result = this.inceptionRecordRepository.save(inceptionRecord);

		if (this.inceptionRecordRepository.exists(inceptionRecord.getId()))
			this.checkByPrincipal(inceptionRecord);
		else {
			History history;

			history = this.historyService.findByPrincipal();

			this.historyService.addInceptionRecord(history, result);
		}

		return result;
	}

	// Other business methods --------------------------
	private void checkByPrincipal(final InceptionRecord inceptionRecord) {
		History history;

		history = this.historyService.findByPrincipal();

		Assert.isTrue(history.getInceptionRecord().equals(inceptionRecord));
	}

}
