
package services;

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
}
