
package services;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.TranslationPositionRepository;
import domain.TranslationPosition;

@Service
@Transactional
public class TranslationPositionService {

	// Managed repository --------------------
	@Autowired
	private TranslationPositionRepository	translationPositionRepository;

	// Other supporting services --------------
	@Autowired
	private CustomisationService			customisationService;

	@Autowired
	private UtilityService					utilityService;


	// Constructors ---------------------------
	public TranslationPositionService() {
		super();
	}

	// Simple CRUD methods -------------------
	public TranslationPosition findOne(final int translationPositionId) {
		TranslationPosition result;

		result = this.translationPositionRepository.findOne(translationPositionId);

		return result;
	}

	public Collection<TranslationPosition> findAll() {
		Collection<TranslationPosition> results;

		results = this.translationPositionRepository.findAll();

		return results;
	}

	public TranslationPosition create() {
		TranslationPosition result;

		result = new TranslationPosition();

		return result;
	}

	public TranslationPosition save(final TranslationPosition translationPosition) {
		Assert.notNull(translationPosition);

		TranslationPosition result;
		List<String> languages;
		String languages_str;

		languages_str = this.customisationService.find().getLanguages();
		languages = this.utilityService.ListByString(languages_str);

		Assert.isTrue(languages.contains(translationPosition.getLanguage()));

		result = this.translationPositionRepository.save(translationPosition);

		return result;
	}

	public void delete(final TranslationPosition translationPosition) {
		Assert.notNull(translationPosition);
		Assert.isTrue(this.translationPositionRepository.exists(translationPosition.getId()));

		this.translationPositionRepository.delete(translationPosition);
	}

	// Other business methods ----------------

	// Protected methods ---------------------
	public TranslationPosition findByLanguagePosition(final int positionId, final String language) {
		TranslationPosition result;

		result = this.translationPositionRepository.findByLanguagePosition(positionId, language);

		return result;
	}

	// Private methods -----------------------
}
