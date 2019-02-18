
package services;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.PositionRepository;
import domain.Position;
import domain.TranslationPosition;

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

		// Remove position::translationPositions
		for (final TranslationPosition tp : position.getTranslationPositions())
			this.translationPositionService.delete(tp);

		this.positionRepository.delete(position);
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

	// TODO: 
	//	public Position reconstruct(PositionForm positionForm) {
	//		
	//	}

	// Protected methods -------------------------

	// Private methods ---------------------------
	private Integer isUsedPosition(final int positionId) {
		Integer result;

		result = this.positionRepository.isUsedPosition(positionId);

		return result;
	}

	private boolean validLanguages(final Position position) {
		boolean result;
		Map<String, Integer> map;
		Collection<TranslationPosition> translationPositions;
		Collection<String> languages;
		Integer valor;

		map = new HashMap<String, Integer>();
		languages = this.customisationService.find().getLanguages();
		translationPositions = position.getTranslationPositions();
		valor = 0;
		result = true;

		Assert.isTrue(translationPositions.size() == languages.size());

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
