
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import repositories.BrotherhoodRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Area;
import domain.Brotherhood;
import forms.BrotherhoodForm;

@Service
@Transactional
public class BrotherhoodService {

	// Managed repository --------------------------

	@Autowired
	private BrotherhoodRepository	brotherhoodRepository;

	// Other supporting services -------------------

	@Autowired
	private ActorService			actorService;


	// Constructors -------------------------------

	public BrotherhoodService() {
		super();
	}

	// Simple CRUD methods ------------------------

	public Brotherhood create() {
		Brotherhood result;

		result = new Brotherhood();
		result.setUserAccount(this.actorService.createUserAccount(Authority.BROTHERHOOD));

		return result;
	}

	public Brotherhood findOne(final int brotherhoodId) {
		Brotherhood result;

		result = this.brotherhoodRepository.findOne(brotherhoodId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Brotherhood> findAll() {
		Collection<Brotherhood> result;

		result = this.brotherhoodRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Brotherhood save(final Brotherhood brotherhood) {
		Brotherhood result;

		result = (Brotherhood) this.actorService.save(brotherhood);

		return result;
	}

	// Other business methods ---------------------

	public Collection<Brotherhood> findLargest() {
		Collection<Brotherhood> result;

		result = this.brotherhoodRepository.findLargest();

		return result;
	}

	public Collection<Brotherhood> findSmallest() {
		Collection<Brotherhood> result;

		result = this.brotherhoodRepository.findSmallest();

		return result;
	}

	public Brotherhood findByPrincipal() {
		Brotherhood result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();

		result = this.findByUserAccount(userAccount.getId());
		Assert.notNull(result);

		return result;
	}

	private Brotherhood findByUserAccount(final int userAccountId) {
		Brotherhood result;

		result = this.brotherhoodRepository.findByUserAccount(userAccountId);

		return result;
	}

	public Brotherhood findBrotherhoodByProcession(final int processionId) {
		Brotherhood brotherhood;

		brotherhood = this.brotherhoodRepository.findBrotherhoodByProcession(processionId);

		return brotherhood;

	}

	public Collection<Brotherhood> findByMemberId(final int memberId) {
		Collection<Brotherhood> result;

		result = this.brotherhoodRepository.findByMemberId(memberId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Brotherhood> findBrotherhoodFromArea(final Area area) {
		Collection<Brotherhood> brotherhoods;

		brotherhoods = this.brotherhoodRepository.findBrotherhoodFromArea(area.getId());

		return brotherhoods;
	}
	public Brotherhood findBrotherhoodToSelectArea() {
		Brotherhood result;

		result = this.findByPrincipal();

		Assert.isNull(result.getArea());

		return result;
	}

	public Brotherhood reconstruct(final BrotherhoodForm brotherhoodForm, final BindingResult binding) {
		final Brotherhood result;

		result = this.findBrotherhoodToSelectArea();
		result.setArea(brotherhoodForm.getArea());
		return result;
	}

}
