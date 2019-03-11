
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ProclaimRepository;
import domain.Chapter;
import domain.Proclaim;

@Service
@Transactional
public class ProclaimService {

	// Managed repository ----------------------------
	@Autowired
	private ProclaimRepository	proclaimRepository;

	// Other supporting services ---------------------
	@Autowired
	private ChapterService		chapterService;

	@Autowired
	private UtilityService		utilityService;


	// Constructors ----------------------------------
	public ProclaimService() {
		super();
	}

	// Simple CRUD methods ---------------------------
	public Collection<Proclaim> findAll() {
		Collection<Proclaim> results;

		results = this.proclaimRepository.findAll();

		return results;
	}

	public Proclaim findOne(final int proclaimId) {
		Proclaim result;

		result = this.proclaimRepository.findOne(proclaimId);

		return result;
	}

	public Proclaim create() {
		Proclaim result;
		Chapter principal;
		Date publishedMoment;

		principal = this.chapterService.findByPrincipal();
		publishedMoment = this.utilityService.current_moment();

		result = new Proclaim();
		result.setChapter(principal);
		result.setPublishedMoment(publishedMoment);

		return result;
	}

	public Proclaim save(final Proclaim proclaim) {
		Assert.notNull(proclaim);
		Assert.isTrue(proclaim.getId() == 0);
		this.checkByPrincipal(proclaim);

		Proclaim result;

		result = this.proclaimRepository.save(proclaim);

		return result;
	}

	// Other business methods -----------------------
	private void checkByPrincipal(final Proclaim proclaim) {
		Chapter principal;

		principal = this.chapterService.findByPrincipal();

		Assert.isTrue(principal.equals(proclaim.getChapter()));
	}

}