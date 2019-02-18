
package services;

import java.util.Collection;

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
		Collection<String> languages;

		languages = this.customisationService.find().getLanguages();

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
	protected TranslationPosition findByLanguagePosition(final int positionId, final String language) {
		TranslationPosition result;

		result = this.translationPositionRepository.findByLanguagePosition(positionId, language);

		return result;
	}

	// Private methods -----------------------
}
