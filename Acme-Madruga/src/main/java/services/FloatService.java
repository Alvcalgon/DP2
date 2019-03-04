
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

		this.utilityService.checkPicture(floatt.getPictures());
		result = this.floatRepository.save(floatt);

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
		final Float result, floatStore;

		result = new Float();

		if (floatForm.getId() == 0)
			result.setBrotherhood(this.brotherhoodService.findByPrincipal());
		else {
			floatStore = this.findOne(floatForm.getId());
			result.setBrotherhood(floatStore.getBrotherhood());

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
	// Other business methods ---------------------
	public Collection<Float> findFloatByBrotherhood(final int brotherhoodId) {
		Collection<Float> floats;

		floats = this.floatRepository.findFloatByBrotherhood(brotherhoodId);

		return floats;

	}
	public String validateTitle(final FloatForm floatForm, final BindingResult binding) {
		String result;

		result = floatForm.getTitle();
		if (result.equals("") || result.equals(null))
			binding.rejectValue("title", "float.error.blank", "Must not be blank");

		return result;
	}
	public String validateDescription(final FloatForm floatForm, final BindingResult binding) {
		String result;

		result = floatForm.getTitle();
		if (result.equals("") || result.equals(null))
			binding.rejectValue("description", "float.error.blank", "Must not be blank");

		return result;
	}

}
