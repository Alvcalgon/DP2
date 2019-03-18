
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

	@Autowired
	private UtilityService				utilityService;


	// Constructors ------------------------------------

	public InceptionRecordService() {
		super();
	}

	// Simple CRUD methods -----------------------------

	public InceptionRecord create() {
		InceptionRecord result;
		Assert.isNull(this.historyService.findByPrincipal());

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

	public InceptionRecord findOneEdit(final int inceptionRecordId) {
		Assert.isTrue(inceptionRecordId != 0);
		InceptionRecord result;

		result = this.inceptionRecordRepository.findOne(inceptionRecordId);

		this.checkByPrincipal(result);
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
		this.utilityService.checkPicture(inceptionRecord.getPhotos());

		InceptionRecord result;

		if (inceptionRecord.getId() == 0) {
			History history;
			history = this.historyService.findByPrincipal();
			result = this.inceptionRecordRepository.save(inceptionRecord);
			this.historyService.addInceptionRecord(history, result);
		} else {
			this.checkByPrincipal(inceptionRecord);
			result = this.inceptionRecordRepository.save(inceptionRecord);
		}

		return result;
	}

	// Other business methods --------------------------
	private void checkByPrincipal(final InceptionRecord inceptionRecord) {
		History history;

		history = this.historyService.findByPrincipal();

		Assert.isTrue(history.getInceptionRecord().equals(inceptionRecord));
	}

	protected void flush() {
		this.inceptionRecordRepository.flush();
	}
}
