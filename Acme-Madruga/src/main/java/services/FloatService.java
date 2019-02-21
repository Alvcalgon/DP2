
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.FloatRepository;
import domain.Brotherhood;
import domain.Float;

@Service
@Transactional
public class FloatService {

	// Managed repository --------------------------

	@Autowired
	private FloatRepository		floatRepository;

	// Other supporting services -------------------

	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Constructors -------------------------------

	public FloatService() {
		super();
	}

	// Simple CRUD methods ------------------------
	public Float create() {
		Float result;
		final Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByPrincipal();

		result = new Float();

		result.setBrotherhood(brotherhood);

		return result;
	}
	public Float save(final Float paradeFloat) {
		Assert.notNull(paradeFloat);

		Float result;

		result = this.floatRepository.save(paradeFloat);

		return result;
	}

	public void delete(final Float paradeFloat) {
		Assert.notNull(paradeFloat);
		Assert.isTrue(this.floatRepository.exists(paradeFloat.getId()));

		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByPrincipal();

		Assert.isTrue(paradeFloat.getBrotherhood().equals(brotherhood));

		this.floatRepository.delete(paradeFloat);
	}

	public Float findOne(final int floatId) {
		Float result;

		result = this.floatRepository.findOne(floatId);

		return result;
	}

	public Collection<Float> findAll() {
		Collection<Float> results;

		results = this.floatRepository.findAll();

		return results;
	}

	public Float findOneToEdit(final int floatId) {
		Float result;
		Brotherhood brotherhood;

		result = this.floatRepository.findOne(floatId);
		brotherhood = this.brotherhoodService.findByPrincipal();

		Assert.notNull(result);
		Assert.isTrue(result.getBrotherhood().equals(brotherhood));

		return result;
	}

	// Other business methods ---------------------
	public Collection<Float> findFloatByBrotherhood(final int brotherhoodId) {
		Collection<Float> floats;

		floats = this.floatRepository.findFloatByBrotherhood(brotherhoodId);

		return floats;

	}

}
