
package services;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SponsorshipRepository;
import domain.CreditCard;
import domain.Parade;
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
	private ParadeService			paradeService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private Validator				validator;


	// Constructors ---------------------------------------

	public SponsorshipService() {
		super();
	}

	// Simple CRUD methods --------------------------------

	public Sponsorship create(final int paradeId) {
		Sponsorship result;
		Parade parade;
		Sponsor sponsor;

		parade = this.paradeService.findOne(paradeId);
		sponsor = this.sponsorService.findByPrincipal();
		result = new Sponsorship();

		result.setIsActive(true);
		result.setParade(parade);
		result.setSponsor(sponsor);

		return result;
	}

	public Sponsorship save(final Sponsorship sponsorship) {
		Assert.notNull(sponsorship);
		Assert.isTrue(sponsorship.getParade().getStatus() == "accepted");
		Assert.isTrue(!this.checkIsExpired(sponsorship.getCreditCard()));
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

	public void removeBySponsor(final Sponsorship sponsorship) {
		Assert.notNull(sponsorship);
		Assert.isTrue(this.sponsorshipRepository.exists(sponsorship.getId()));
		this.checkOwner(sponsorship);

		this.remove(sponsorship);
	}

	public void deactivateProcess() {
		Collection<Sponsorship> activeSponsorships;

		activeSponsorships = this.findAllActive();

		for (final Sponsorship s : activeSponsorships)
			if (this.checkIsExpired(s.getCreditCard()))
				this.remove(s);
	}

	public void reactivate(final Sponsorship sponsorship) {
		Assert.notNull(sponsorship);
		Assert.isTrue(this.sponsorshipRepository.exists(sponsorship.getId()));

		sponsorship.setIsActive(true);
	}

	public Sponsorship getRandomSponsorship(final int paradeId) {
		List<Sponsorship> activeSponsorships;
		Sponsorship result;
		int index, numberSponsorships;
		Random random;

		activeSponsorships = this.findActiveByParadeId(paradeId);
		random = new Random();
		numberSponsorships = activeSponsorships.size();

		if (numberSponsorships > 0) {
			index = random.nextInt(numberSponsorships);
			result = activeSponsorships.get(index);
			this.messageService.notificationFare(result);
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

	public Double ratioActiveSponsorship() {
		Double result;

		result = this.sponsorshipRepository.ratioActiveSponsorship();

		return result;
	}

	public Double[] dataSponsorshipPerSponsor() {
		Double[] result;

		result = this.sponsorshipRepository.dataSponsorshipPerSponsor();

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

	private void remove(final Sponsorship sponsorship) {
		sponsorship.setIsActive(false);
	}

	private Collection<Sponsorship> findAllActive() {
		Collection<Sponsorship> result;

		result = this.sponsorshipRepository.findAllActive();
		Assert.notNull(result);

		return result;
	}

	private List<Sponsorship> findActiveByParadeId(final int paradeId) {
		List<Sponsorship> result;

		result = this.sponsorshipRepository.findActiveByParadeId(paradeId);
		Assert.notNull(result);

		return result;
	}

	private void checkOwner(final Sponsorship sponsorship) {
		Sponsor principal;

		principal = this.sponsorService.findByPrincipal();
		Assert.isTrue(sponsorship.getSponsor().equals(principal));
	}

	private boolean checkIsExpired(final CreditCard creditCard) {
		String year, month;
		LocalDate expiration, now;
		boolean result;
		DateTimeFormatter formatter;

		year = creditCard.getExpirationYear();
		month = creditCard.getExpirationMonth();
		formatter = DateTimeFormat.forPattern("yy-MM-dd");
		expiration = LocalDate.parse(year + "-" + month + "-" + "01", formatter);
		expiration = expiration.plusMonths(1).minusDays(1);
		now = LocalDate.now();

		result = now.isAfter(expiration);

		return result;
	}

	protected void flush() {
		this.sponsorshipRepository.flush();
	}
}
