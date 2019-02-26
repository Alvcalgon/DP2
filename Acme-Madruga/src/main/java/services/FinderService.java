
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FinderRepository;
import domain.Customisation;
import domain.Finder;
import domain.Member;
import domain.Procession;

@Service
@Transactional
public class FinderService {

	// Managed repository ---------------------------------

	@Autowired
	private FinderRepository		finderRepository;

	@Autowired
	private MemberService			memberService;

	@Autowired
	private ProcessionService		processionService;

	@Autowired
	private UtilityService			utilityService;

	@Autowired
	private CustomisationService	customisationService;

	@Autowired
	private Validator				validator;


	// Other supporting services --------------------------

	// Constructors ---------------------------------------

	public FinderService() {
		super();
	}

	// Simple CRUD methods --------------------------------

	private Finder create() {
		Finder result;

		result = new Finder();
		result.setKeyword("");
		result.setArea("");
		result.setUpdatedMoment(new Date(Integer.MIN_VALUE));

		return result;
	}

	public void save(final Finder finder) {
		Assert.notNull(finder);

		this.finderRepository.save(finder);
	}

	// Other business methods -----------------------------

	public Finder evaluateSearch(final Finder finder) {
		Page<Procession> processions;
		Pageable pageable;
		Customisation customisation;

		customisation = this.customisationService.find();
		pageable = new PageRequest(0, customisation.getMaxNumberResults());

		if (this.isFinderOutdated(finder.getUpdatedMoment(), customisation.getTimeCachedResults())) {
			processions = this.processionService.searchProcessionFinder(finder, pageable);

			finder.setProcessions(processions.getContent());
		}

		return finder;
	}

	public void clear(final Finder finder) {
		finder.setKeyword("");
		finder.setArea("");
		finder.setMaximumDate(null);
		finder.setMinimumDate(null);
		finder.setUpdatedMoment(new Date(Integer.MIN_VALUE));
	}

	public void removeProcessionToFinder(final Procession procession) {
		Collection<Finder> finders;
		Collection<Procession> processions;

		finders = this.finderRepository.findFinderByProcession(procession.getId());

		for (final Finder f : finders) {
			processions = f.getProcessions();
			for (final Procession p : processions)
				if (p.equals(procession))
					this.finderRepository.delete(f);
		}
	}

	@Transactional(propagation = Propagation.NEVER)
	public Finder reconstruct(final Finder finder, final BindingResult binding) {
		Finder result;

		result = this.finderRepository.findOne(finder.getId());

		result.setKeyword(finder.getKeyword());
		result.setArea(finder.getArea());
		result.setMinimumDate(finder.getMinimumDate());
		result.setMaximumDate(finder.getMaximumDate());

		this.validator.validate(result, binding);

		return result;
	}

	public Finder findByMemberPrincipal() {
		Finder result;
		Member member;

		member = this.memberService.findByPrincipal();
		result = this.finderRepository.findByMemberId(member.getId());
		Assert.notNull(result);

		return result;
	}

	public Double findRatioEmptyVsNonEmpty() {
		Double result;

		result = this.finderRepository.findRatioEmptyVsNonEmpty();

		return result;
	}

	protected void assignNewFinder(final Member member) {
		Finder finder;

		finder = this.create();
		finder.setMember(member);
		this.save(finder);
	}

	private boolean isFinderOutdated(final Date updatedUpdate, final int timeCache) {
		final Boolean result;
		Long time, diff;

		time = this.utilityService.current_moment().getTime() - updatedUpdate.getTime();
		diff = time / (1000 * 60 * 60);
		result = diff > timeCache;

		return result;
	}

}
