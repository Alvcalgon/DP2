
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import repositories.PositionRepository;
import domain.Position;
import domain.TranslationPosition;
import forms.PositionForm;

@Service
@Transactional
public class PositionService {

	// Managed repository ----------------------
	@Autowired
	private PositionRepository			positionRepository;

	// Other supporting services -----------------
	@Autowired
	private CustomisationService		customisationService;

	@Autowired
	private TranslationPositionService	translationPositionService;

	@Autowired
	private UtilityService				utilityService;


	// Constructors ------------------------------
	public PositionService() {
		super();
	}

	// Simple CRUD methods -----------------------
	public Position findOne(final int positionId) {
		Position result;

		result = this.positionRepository.findOne(positionId);

		return result;
	}

	public Collection<Position> findAll() {
		Collection<Position> results;

		results = this.positionRepository.findAll();

		return results;
	}

	public Position create() {
		Position result;

		result = new Position();

		result.setTranslationPositions(Collections.<TranslationPosition> emptySet());

		return result;
	}

	public Position save(final Position position) {
		Assert.notNull(position);
		Assert.isTrue(this.validLanguages(position));

		Position result;

		result = this.positionRepository.save(position);

		return result;
	}

	public void delete(final Position position) {
		Assert.notNull(position);
		Assert.isTrue(this.positionRepository.exists(position.getId()) && this.isUsedPosition(position.getId()) == 0);

		Collection<TranslationPosition> translationPositions;
		translationPositions = position.getTranslationPositions();

		this.positionRepository.delete(position);

		// Remove position::translationPositions
		for (final TranslationPosition tp : translationPositions)
			this.translationPositionService.delete(tp);
	}

	// Other business methods --------------------
	public SortedMap<Integer, String> positionsByLanguages(final Collection<Position> positions, final String language) {
		SortedMap<Integer, String> result;
		String positionName;
		TranslationPosition tp;

		result = new TreeMap<Integer, String>();

		for (final Position p : positions) {
			tp = this.translationPositionService.findByLanguagePosition(p.getId(), language);
			positionName = tp.getName();

			result.put(p.getId(), positionName);
		}

		return result;
	}

	public Position reconstruct(final PositionForm positionForm) {
		Position result;
		List<TranslationPosition> translationPositions;
		TranslationPosition en_position, es_position, en_saved, es_saved;

		if (positionForm.getId() == 0) {
			result = this.create();
			translationPositions = new ArrayList<TranslationPosition>();

			en_position = this.translationPositionService.create();
			en_position.setLanguage("en");
			en_position.setName(positionForm.getEn_name());

			es_position = this.translationPositionService.create();
			es_position.setLanguage("es");
			es_position.setName(positionForm.getEs_name());

			en_saved = this.translationPositionService.save(en_position);
			es_saved = this.translationPositionService.save(es_position);

			translationPositions.add(en_saved);
			translationPositions.add(es_saved);

			result.setTranslationPositions(translationPositions);
		} else {
			result = this.findOne(positionForm.getId());

			en_saved = this.translationPositionService.findByLanguagePosition(positionForm.getId(), "en");
			en_saved.setName(positionForm.getEn_name());

			es_saved = this.translationPositionService.findByLanguagePosition(positionForm.getId(), "es");
			es_saved.setName(positionForm.getEs_name());
		}

		return result;
	}

	public String validateName(final String language, final String nameAttribute, final String valueAttribute, final BindingResult binding) {
		String result;

		result = valueAttribute;
		if (result.equals("") || result.equals(null))
			if (language.equals("en"))
				binding.rejectValue(nameAttribute, "category.error.blank", "Must not be blank");
			else
				binding.rejectValue(nameAttribute, "category.error.blank", "No debe ser blanco");

		return result;
	}

	public List<Integer> findHistogramValues() {
		List<Integer> results;

		results = this.positionRepository.findHistogramValues();

		return results;
	}

	public List<String> findHistogramLabels(final String language) {
		List<String> results;

		results = this.positionRepository.findHistogramLabels(language);

		return results;
	}

	public Integer isUsedPosition(final int positionId) {
		Integer result;

		result = this.positionRepository.isUsedPosition(positionId);

		return result;
	}

	// Protected methods -------------------------

	// Private methods ---------------------------
	private boolean validLanguages(final Position position) {
		boolean result;
		Map<String, Integer> map;
		Collection<TranslationPosition> translationPositions;
		Integer valor;
		String languages_str;
		List<String> languages;

		languages_str = this.customisationService.find().getLanguages();
		languages = this.utilityService.ListByString(languages_str);

		translationPositions = position.getTranslationPositions();
		valor = 0;
		result = true;

		Assert.isTrue(translationPositions.size() == languages.size());

		map = new HashMap<String, Integer>();
		for (final String s : languages)
			map.put(s, 0);

		for (final TranslationPosition tp : translationPositions) {
			valor = map.get(tp.getLanguage());
			valor++;
			map.put(tp.getLanguage(), valor);
		}

		for (final Integer i : map.values())
			result = result && i.equals(1);

		return result;
	}

}
