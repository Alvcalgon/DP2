
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FloatRepository;
import domain.Brotherhood;
import domain.Float;
import forms.FloatForm;

@Service
@Transactional
public class FloatService {

	// Managed repository --------------------------

	@Autowired
	private FloatRepository		floatRepository;

	// Other supporting services -------------------

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private UtilityService		utilityService;

	@Autowired
	private Validator			validator;


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
	public Float save(final Float floatt) {
		Assert.notNull(floatt);

		Float result;

		result = this.floatRepository.save(floatt);
		this.utilityService.checkPicture(floatt.getPictures());

		return result;
	}

	public void delete(final Float floatt) {
		Assert.notNull(floatt);
		Assert.isTrue(this.floatRepository.exists(floatt.getId()));

		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByPrincipal();

		Assert.isTrue(floatt.getBrotherhood().equals(brotherhood));

		this.floatRepository.delete(floatt);
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

	public Float reconstruct(final FloatForm floatForm, final BindingResult binding) {
		final Float result;
		final Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByPrincipal();

		if (floatForm.getId() == 0) {

			result = this.create();
			result.setTitle(floatForm.getTitle());
			result.setId(floatForm.getId());
			result.setPictures(floatForm.getPictures());
			result.setDescription(floatForm.getDescription());
			result.setVersion(floatForm.getVersion());
		} else {
			result = this.findOneToEdit(floatForm.getId());
			result.setTitle(floatForm.getTitle());
			result.setPictures(floatForm.getPictures());
			result.setDescription(floatForm.getDescription());
			result.setId(floatForm.getId());
			result.setVersion(floatForm.getVersion());
			this.utilityService.checkPicture(result.getPictures());
		}

		this.validator.validate(result, binding);
		return result;
	}
	// Other business methods ---------------------
	public Collection<Float> findFloatByBrotherhood(final int brotherhoodId) {
		Collection<Float> floats;

		floats = this.floatRepository.findFloatByBrotherhood(brotherhoodId);

		return floats;

	}

}
