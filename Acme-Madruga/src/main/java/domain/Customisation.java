
package domain;

import java.util.Collection;

public class Customisation extends DomainEntity {

	// Constructor

	public Customisation() {
		super();
	}


	// Attributes

	private String				name;
	private String				banner;
	private String				spanishWelcomeMessage;
	private String				englishWelcomeMessage;
	private String				countryCode;
	private Collection<String>	positions;
	private int					timeCachedResults;
	private int					maxNumberResults;
	private Collection<String>	priorities;
	private Collection<String>	spamWords;
	private Collection<String>	positiveWords;
	private Collection<String>	negativeWords;


	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getBanner() {
		return this.banner;
	}

	public void setBanner(final String banner) {
		this.banner = banner;
	}

	public String getSpanishWelcomeMessage() {
		return this.spanishWelcomeMessage;
	}

	public void setSpanishWelcomeMessage(final String spanishWelcomeMessage) {
		this.spanishWelcomeMessage = spanishWelcomeMessage;
	}

	public String getEnglishWelcomeMessage() {
		return this.englishWelcomeMessage;
	}

	public void setEnglishWelcomeMessage(final String englishWelcomeMessage) {
		this.englishWelcomeMessage = englishWelcomeMessage;
	}

	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(final String countryCode) {
		this.countryCode = countryCode;
	}

	public Collection<String> getPositions() {
		return this.positions;
	}

	public void setPositions(final Collection<String> positions) {
		this.positions = positions;
	}

	public int getTimeCachedResults() {
		return this.timeCachedResults;
	}

	public void setTimeCachedResults(final int timeCachedResults) {
		this.timeCachedResults = timeCachedResults;
	}

	public int getMaxNumberResults() {
		return this.maxNumberResults;
	}

	public void setMaxNumberResults(final int maxNumberResults) {
		this.maxNumberResults = maxNumberResults;
	}

	public Collection<String> getPriorities() {
		return this.priorities;
	}

	public void setPriorities(final Collection<String> priorities) {
		this.priorities = priorities;
	}

	public Collection<String> getSpamWords() {
		return this.spamWords;
	}

	public void setSpamWords(final Collection<String> spamWords) {
		this.spamWords = spamWords;
	}

	public Collection<String> getPositiveWords() {
		return this.positiveWords;
	}

	public void setPositiveWords(final Collection<String> positiveWords) {
		this.positiveWords = positiveWords;
	}

	public Collection<String> getNegativeWords() {
		return this.negativeWords;
	}

	public void setNegativeWords(final Collection<String> negativeWords) {
		this.negativeWords = negativeWords;
	}

}
