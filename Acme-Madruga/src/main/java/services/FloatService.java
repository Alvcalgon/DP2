
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	//TODO: no se si hace falta, si no quitar
	public Page<Float> findAllPage(final Pageable pageable) {
		Page<Float> result;

		result = this.floatRepository.findAll(pageable);

		Assert.notNull(result);

		return result;

	}

	// Other business methods ---------------------

	public Page<Float> findFloatByBrotherhood(final int brotherhoodId, final Pageable pageable) {
		Page<Float> floats;

		floats = this.floatRepository.findFloatByBrotherhood(brotherhoodId, pageable);

		return floats;

	}

}
