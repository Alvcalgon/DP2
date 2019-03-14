
package services;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SponsorshipRepository;
import domain.Sponsor;
import domain.Sponsorship;

@Service
@Transactional
public class SponsorshipService {

	// Managed repository ---------------------------------

	@Autowired
	private SponsorshipRepository	sponsorshipRepository;

	// Other supporting services --------------------------

	@Autowired
	private SponsorService			sponsorService;

	@Autowired
	private Validator				validator;


	// Constructors ---------------------------------------

	public SponsorshipService() {
		super();
	}

	// Simple CRUD methods --------------------------------

	public Sponsorship save(final Sponsorship sponsorship) {
		Assert.notNull(sponsorship);
		this.checkOwner(sponsorship);

		Sponsorship saved;

		saved = this.sponsorshipRepository.save(sponsorship);

		return saved;
	}

	public Sponsorship findOne(final int sponsorshipId) {
		Sponsorship result;

		result = this.sponsorshipRepository.findOne(sponsorshipId);
		Assert.notNull(result);

		return result;
	}

	public Sponsorship findOneToEditDisplay(final int sponsorshipId) {
		Sponsorship result;

		result = this.sponsorshipRepository.findOne(sponsorshipId);
		this.checkOwner(result);

		return result;
	}

	// Other business methods -----------------------------

	private void remove(final Sponsorship sponsorship) {
		Assert.notNull(sponsorship);
		Assert.isTrue(this.sponsorshipRepository.exists(sponsorship.getId()));

		sponsorship.setIsActive(false);
	}

	public void removeBySponsor(final Sponsorship sponsorship) {
		this.checkOwner(sponsorship);
		this.remove(sponsorship);
	}

	//	public void removeByAdmin(final Sponsorship sponsorship) {
	//		this.remove(sponsorship);
	//	}

	public void reactivate(final Sponsorship sponsorship) {
		Assert.notNull(sponsorship);
		Assert.isTrue(this.sponsorshipRepository.exists(sponsorship.getId()));

		sponsorship.setIsActive(true);
	}

	public Sponsorship getRandomSponsorship(final int paradeId) {
		List<Sponsorship> allSponsorships;
		Sponsorship result;
		int index, numberSponsorships;
		Random random;

		allSponsorships = this.findByParadeId(paradeId);
		random = new Random();
		numberSponsorships = allSponsorships.size();

		if (numberSponsorships > 0) {
			index = random.nextInt(numberSponsorships);
			result = allSponsorships.get(index);
		} else
			result = null;

		return result;
	}

	public Collection<Sponsorship> findAllByPrincipal() {
		Collection<Sponsorship> result;
		Sponsor sponsor;

		sponsor = this.sponsorService.findByPrincipal();
		result = this.sponsorshipRepository.findAllBySponsorId(sponsor.getId());
		Assert.notNull(result);

		return result;
	}

	// Ancillary methods ----------------------------------

	public Sponsorship reconstruct(final Sponsorship sponsorship, final BindingResult binding) {
		Sponsorship result, sponsorshipStored;

		result = new Sponsorship();
		sponsorshipStored = this.sponsorshipRepository.findOne(sponsorship.getId());
		// TODO: Tratamiento distinto si llego aquí desde un create?
		result.setId(sponsorship.getId());
		result.setBanner(sponsorship.getBanner().trim());
		result.setCreditCard(sponsorship.getCreditCard());
		result.setIsActive(sponsorshipStored.getIsActive());
		result.setParade(sponsorshipStored.getParade());
		result.setSponsor(sponsorshipStored.getSponsor());
		result.setTargetURL(sponsorship.getTargetURL().trim());
		result.setVersion(sponsorshipStored.getVersion());

		this.validator.validate(result, binding);

		return result;
	}

	private List<Sponsorship> findByParadeId(final int paradeId) {
		List<Sponsorship> result;

		result = this.sponsorshipRepository.findByParadeId(paradeId);
		Assert.notNull(result);

		return result;
	}

	// Ancillary methods ----------------------------------

	public Double[] dataSponsorshipPerSponsor() {
		Double[] result;

		result = this.sponsorshipRepository.dataSponsorshipPerSponsor();

		return result;
	}

	private void checkOwner(final Sponsorship sponsorship) {
		Sponsor principal;

		principal = this.sponsorService.findByPrincipal();
		Assert.isTrue(sponsorship.getSponsor().equals(principal));
	}

}
