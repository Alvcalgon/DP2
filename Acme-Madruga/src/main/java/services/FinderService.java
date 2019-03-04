
package services;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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
		result.setProcessions(Collections.<Procession> emptySet());

		return result;
	}

	public void save(final Finder finder) {
		Assert.notNull(finder);
		Assert.isTrue(this.validDates(finder));
		this.checkOwner(finder);

		Finder saved;
		Pageable pageable;
		Customisation customisation;

		customisation = this.customisationService.find();
		pageable = new PageRequest(0, customisation.getMaxNumberResults());

		saved = this.finderRepository.save(finder);
		this.processionService.searchProcessionFinder(saved, pageable);
	}

	// Other business methods -----------------------------

	public Finder evaluateSearch(final Finder finder) {
		Pageable pageable;
		Customisation customisation;

		customisation = this.customisationService.find();
		pageable = new PageRequest(0, customisation.getMaxNumberResults());

		if (this.isFinderOutdated(finder.getUpdatedMoment(), customisation.getTimeCachedResults()))
			this.processionService.searchProcessionFinder(finder, pageable);

		return finder;
	}

	public void clear(final Finder finder) {
		this.checkOwner(finder);

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

	public Finder reconstruct(final Finder finder, final BindingResult binding) {
		Finder result, finderStored;

		result = new Finder();
		finderStored = this.finderRepository.findOne(finder.getId());

		result.setId(finder.getId());
		result.setKeyword(finder.getKeyword().trim());
		result.setArea(finder.getArea().trim());
		result.setMinimumDate(finder.getMinimumDate());
		result.setMaximumDate(finder.getMaximumDate());
		result.setMember(finderStored.getMember());
		result.setProcessions(finderStored.getProcessions());
		result.setUpdatedMoment(finderStored.getUpdatedMoment());
		result.setVersion(finderStored.getVersion());

		this.validator.validate(result, binding);
		if (!this.validDates(result))
			binding.rejectValue("minimumDate", null, "Minimum date cannot be later than maximum date.");

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
		Long diff, milisTimeCache;

		diff = this.utilityService.current_moment().getTime() - updatedUpdate.getTime();
		milisTimeCache = TimeUnit.HOURS.toMillis(timeCache);
		result = diff >= milisTimeCache;

		return result;
	}

	private boolean validDates(final Finder finder) {
		boolean result;

		if (finder.getMaximumDate() != null && finder.getMinimumDate() != null)
			result = finder.getMinimumDate().before(finder.getMaximumDate());
		else
			result = true;

		return result;
	}

	private void checkOwner(final Finder finder) {
		Member principal;

		if (finder.getId() != 0) {
			principal = this.memberService.findByPrincipal();
			Assert.isTrue(finder.getMember().equals(principal));
		}
	}
}
