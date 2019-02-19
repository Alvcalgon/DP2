
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
	private FloatRepository	floatRepository;


	// Other supporting services -------------------

	//TODO: hacer cuando se suba brotherhoodService
	//	@Autowired
	//	private BrotherhoodService	brotherhoodService;

	// Constructors -------------------------------

	public FloatService() {
		super();
	}

	// Simple CRUD methods ------------------------
	public Float create() {
		Float result;
		final Brotherhood brotherhood;

		//TODO: hacer cuando se suba brotherhoodService
		//		brotherhood = brotherhoodService.findByPrincipal();

		result = new Float();
		//		result.setBrotherhood(brotherhood);

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

	// Other business methods ---------------------

}
