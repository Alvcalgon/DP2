
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.HistoryRepository;
import domain.Brotherhood;
import domain.History;
import domain.InceptionRecord;
import domain.LegalRecord;
import domain.LinkRecord;
import domain.MiscellaneousRecord;
import domain.PeriodRecord;

@Service
@Transactional
public class HistoryService {

	// Managed repository ------------------------------
	@Autowired
	private HistoryRepository	historyRepository;

	// Supporting services -----------------------------
	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Constructors ------------------------------------
	public HistoryService() {
		super();
	}

	// Simple CRUD methods -----------------------------

	public History create() {
		History result;
		Brotherhood brotherhood;

		result = new History();
		brotherhood = this.brotherhoodService.findByPrincipal();

		result.setBrotherhood(brotherhood);
		result.setPeriodRecords(new HashSet<PeriodRecord>());
		result.setMiscellaneousRecords(new HashSet<MiscellaneousRecord>());
		result.setLegalRecords(new HashSet<LegalRecord>());
		result.setLinkRecords(new HashSet<LinkRecord>());

		return result;
	}

	public History findOne(final int historyId) {
		Assert.isTrue(historyId != 0);

		History result;

		result = this.historyRepository.findOne(historyId);

		return result;

	}

	public History save(final History history) {
		Assert.notNull(history);
		Assert.isTrue(!(this.historyRepository.exists(history.getId())));

		History result;

		result = this.historyRepository.save(history);

		return result;
	}

	// Other business methods --------------------------

	protected void addPeriodRecord(final History history, final PeriodRecord periodRecord) {
		history.getPeriodRecords().add(periodRecord);
	}

	protected void removePeriodRecord(final History history, final PeriodRecord periodRecord) {
		history.getPeriodRecords().remove(periodRecord);
	}

	protected void addLegalRecord(final History history, final LegalRecord legalRecord) {
		history.getLegalRecords().add(legalRecord);
	}

	protected void removeLegalRecord(final History history, final LegalRecord legalRecord) {
		history.getLegalRecords().remove(legalRecord);
	}

	protected void addMiscellaneousRecord(final History history, final MiscellaneousRecord miscellaneousRecord) {
		history.getMiscellaneousRecords().add(miscellaneousRecord);
	}

	protected void removeMiscellaneousRecord(final History history, final MiscellaneousRecord miscellaneousRecord) {
		history.getMiscellaneousRecords().remove(miscellaneousRecord);
	}

	public void addInceptionRecord(final History history, final InceptionRecord inceptionRecord) {
		history.setInceptionRecord(inceptionRecord);
	}

	protected void addLinkRecord(final History history, final LinkRecord linkRecord) {
		history.getLinkRecords().add(linkRecord);
	}

	protected void removeLinkRecord(final History history, final LinkRecord linkRecord) {
		history.getLinkRecords().remove(linkRecord);
	}

	public History findHistoryByBrotherhood(final int brotherhoodId) {
		History history;

		history = this.historyRepository.findHistoryByBrotherhood(brotherhoodId);

		return history;
	}

	public History findByPrincipal() {
		History history;

		history = this.findHistoryByBrotherhood(this.brotherhoodService.findByPrincipal().getId());

		return history;
	}

	public History findHistoryByInceptionRecord(final int inceptionRecordId) {
		History history;

		history = this.historyRepository.findHistoryByInceptionRecord(inceptionRecordId);

		return history;
	}

	public History findHistoryByPeriodRecord(final int periodRecordId) {
		History history;

		history = this.historyRepository.findHistoryByPeriodRecord(periodRecordId);

		return history;
	}

	public History findHistoryByMiscellaneousRecord(final int miscellaneousRecordId) {
		History history;

		history = this.historyRepository.findHistoryByMiscellaneousRecord(miscellaneousRecordId);

		return history;
	}

	public History findHistoryByLegalRecord(final int legalRecordId) {
		History history;

		history = this.historyRepository.findHistoryByLegalRecord(legalRecordId);

		return history;
	}

	public History findHistoryByLinkRecord(final int linkRecordId) {
		History history;

		history = this.historyRepository.findHistoryByLinkRecord(linkRecordId);

		return history;
	}

	public Brotherhood findBrotherhoohLargestHistory() {
		Brotherhood brotherhood;

		brotherhood = this.historyRepository.findBrotherhoohLargestHistory();

		return brotherhood;
	}

	public Double[] findDataNumberRecordsPerHistory() {
		Double[] result;

		result = this.historyRepository.findDataNumberRecordsPerHistory();

		return result;
	}

	public Collection<Brotherhood> findBrotherhoohsLargestHistoryAvg() {
		Collection<Brotherhood> brotherhoods;

		brotherhoods = this.historyRepository.findBrotherhoohsLargestHistoryAvg();

		return brotherhoods;
	}
}
