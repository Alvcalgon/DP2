
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.LinkRecordRepository;
import domain.Brotherhood;
import domain.History;
import domain.LinkRecord;

@Service
@Transactional
public class LinkRecordService {

	// Managed repository ------------------------------
	@Autowired
	private LinkRecordRepository	linkRecordRepository;

	// Supporting services -----------------------------
	@Autowired
	private HistoryService			historyService;


	// Constructors ------------------------------------

	public LinkRecordService() {
		super();
	}
	// Simple CRUD methods -----------------------------

	public LinkRecord create() {
		LinkRecord result;
		Assert.isTrue(!(this.historyService.findByPrincipal() == null));

		result = new LinkRecord();

		return result;
	}

	public LinkRecord findOne(final int linkRecordId) {
		Assert.isTrue(linkRecordId != 0);
		LinkRecord result;

		result = this.linkRecordRepository.findOne(linkRecordId);
		Assert.notNull(result);

		return result;

	}

	public LinkRecord findOneEdit(final int linkRecordId) {
		Assert.isTrue(linkRecordId != 0);
		LinkRecord result;

		result = this.linkRecordRepository.findOne(linkRecordId);

		this.checkBrotherhoodLink(result);
		Assert.notNull(result);

		return result;

	}

	protected Collection<LinkRecord> findAll() {
		Collection<LinkRecord> results;

		results = this.linkRecordRepository.findAll();
		Assert.notNull(results);

		return results;
	}

	public LinkRecord save(final LinkRecord linkRecord) {
		Assert.notNull(linkRecord);
		this.checkBrotherhoodLink(linkRecord);

		LinkRecord result;

		if (linkRecord.getId() == 0) {
			History history;
			history = this.historyService.findByPrincipal();
			result = this.linkRecordRepository.save(linkRecord);
			this.historyService.addLinkRecord(history, result);
		} else {
			this.checkByPrincipal(linkRecord);
			result = this.linkRecordRepository.save(linkRecord);
		}

		return result;
	}
	public void delete(final LinkRecord linkRecord) {
		Assert.notNull(linkRecord);
		Assert.isTrue(linkRecord.getId() != 0);
		this.checkByPrincipal(linkRecord);

		History history;

		history = this.historyService.findByPrincipal();

		this.historyService.removeLinkRecord(history, linkRecord);
		this.linkRecordRepository.delete(linkRecord);
	}

	public void deleteLinkRecordsLinkedWithBrotherhood(final Brotherhood brotherhood) {
		Collection<LinkRecord> linkRecords;

		linkRecords = this.linkRecordRepository.linkRecordsLinkedWithBrotherhood(brotherhood.getId());

		this.linkRecordRepository.deleteInBatch(linkRecords);
	}

	// Other business methods --------------------------
	private void checkByPrincipal(final LinkRecord linkRecord) {
		History history;

		history = this.historyService.findByPrincipal();

		Assert.isTrue(history.getLinkRecords().contains(linkRecord));
	}

	private void checkBrotherhoodLink(final LinkRecord linkRecord) {
		History history;

		history = this.historyService.findByPrincipal();

		Assert.isTrue(!(linkRecord.getBrotherhood().equals(history.getBrotherhood())));

	}
	protected void flush() {
		this.linkRecordRepository.flush();
	}
}
