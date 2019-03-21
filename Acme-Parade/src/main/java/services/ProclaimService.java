
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

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


	@Autowired
	private Validator	validator;


	// Other business methods -----------------------
	public Proclaim reconstruct(final Proclaim proclaim, final BindingResult binding) {
		Proclaim result;

		result = this.create();
		result.setText(proclaim.getText().trim());

		this.validator.validate(result, binding);

		return result;
	}

	public Collection<Proclaim> findByPrincipal() {
		Collection<Proclaim> results;
		Chapter principal;

		principal = this.chapterService.findByPrincipal();

		results = this.findByChapterId(principal.getId());

		return results;
	}

	public Collection<Proclaim> findByChapterId(final int chapterId) {
		Collection<Proclaim> result;

		result = this.proclaimRepository.findByChapterId(chapterId);
		Assert.notNull(result);

		return result;
	}

	public void deleteProclaims(final Chapter chapter) {
		Collection<Proclaim> proclaims;

		proclaims = this.findByChapterId(chapter.getId());

		this.proclaimRepository.delete(proclaims);
	}

	private void checkByPrincipal(final Proclaim proclaim) {
		Chapter principal;

		principal = this.chapterService.findByPrincipal();

		Assert.isTrue(principal.equals(proclaim.getChapter()));
	}

}
