
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
		this.checkFloatByPrincipal(floatt);

		Float result;

		this.utilityService.checkPicture(floatt.getPictures());
		result = this.floatRepository.save(floatt);

		return result;
	}

	public void delete(final Float floatt) {
		Assert.notNull(floatt);
		Assert.isTrue(this.floatRepository.exists(floatt.getId()));
		this.checkFloatByPrincipal(floatt);

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

		result = this.floatRepository.findOne(floatId);

		this.checkFloatByPrincipal(result);
		Assert.notNull(result);

		return result;
	}

	// Other business methods ---------------------
	public Collection<Float> findFloatByBrotherhood(final int brotherhoodId) {
		Collection<Float> floats;

		floats = this.floatRepository.findFloatByBrotherhood(brotherhoodId);

		return floats;

	}

	public Float reconstruct(final FloatForm floatForm, final BindingResult binding) {
		final Float result, floatStore;

		result = new Float();

		if (floatForm.getId() == 0)
			result.setBrotherhood(this.brotherhoodService.findByPrincipal());
		else {
			floatStore = this.findOneToEdit(floatForm.getId());
			result.setBrotherhood(floatStore.getBrotherhood());
			result.setVersion(floatStore.getVersion());

		}
		result.setId(floatForm.getId());
		result.setPictures(floatForm.getPictures());
		result.setTitle(floatForm.getTitle());
		result.setDescription(floatForm.getDescription());

		this.validator.validate(result, binding);

		return result;
	}

	public FloatForm createForm(final Float floatt) {
		FloatForm floatForm;

		floatForm = new FloatForm();

		floatForm.setDescription(floatt.getDescription());
		floatForm.setId(floatt.getId());
		floatForm.setPictures(floatt.getPictures());
		floatForm.setTitle(floatt.getTitle());

		return floatForm;

	}

	protected void deleteFloatBrotherhood(final Brotherhood brotherhood) {
		Collection<Float> floats;

		floats = this.findFloatByBrotherhood(brotherhood.getId());

		this.floatRepository.delete(floats);
	}

	protected void checkFloatByPrincipal(final Float floatt) {

		Brotherhood brotherhood;

		brotherhood = this.brotherhoodService.findByPrincipal();

		Assert.isTrue(floatt.getBrotherhood().equals(brotherhood));
	}
}
