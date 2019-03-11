
package services;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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


	// Constructors ---------------------------------------

	public SponsorshipService() {
		super();
	}

	// Simple CRUD methods --------------------------------

	// Other business methods -----------------------------

	public Collection<Sponsorship> findAllByPrincipal() {
		Collection<Sponsorship> result;
		Sponsor sponsor;

		sponsor = this.sponsorService.findByPrincipal();
		result = this.sponsorshipRepository.findAllBySponsorId(sponsor.getId());
		Assert.notNull(result);

		return result;
	}

	public Sponsorship getRandomSponsorship(final int paradeId) {
		List<Sponsorship> allSponsorships;
		Sponsorship result;
		int index;
		Random random;

		allSponsorships = this.findByParadeId(paradeId);
		random = new Random();
		index = random.nextInt(allSponsorships.size());
		result = allSponsorships.get(index);

		return result;
	}

	private List<Sponsorship> findByParadeId(final int paradeId) {
		List<Sponsorship> result;

		result = this.sponsorshipRepository.findByParadeId(paradeId);
		Assert.notNull(result);

		return result;
	}

	// Ancillary methods ----------------------------------

}
