
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
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
	private Collection<String>	languages;
	private int					timeCachedResults;
	private int					maxNumberResults;
	private Collection<String>	priorities;
	private Collection<String>	spamWords;
	private Collection<String>	positiveWords;
	private Collection<String>	negativeWords;
	private int					rowLimit;
	private int					columnLimit;


	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	@URL
	public String getBanner() {
		return this.banner;
	}

	public void setBanner(final String banner) {
		this.banner = banner;
	}

	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getSpanishWelcomeMessage() {
		return this.spanishWelcomeMessage;
	}

	public void setSpanishWelcomeMessage(final String spanishWelcomeMessage) {
		this.spanishWelcomeMessage = spanishWelcomeMessage;
	}

	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getEnglishWelcomeMessage() {
		return this.englishWelcomeMessage;
	}

	public void setEnglishWelcomeMessage(final String englishWelcomeMessage) {
		this.englishWelcomeMessage = englishWelcomeMessage;
	}

	@NotBlank
	@Pattern(regexp = "\\+\\d+")
	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(final String countryCode) {
		this.countryCode = countryCode;
	}

	@NotNull
	@NotEmpty
	@ElementCollection
	public Collection<String> getLanguages() {
		return this.languages;
	}

	public void setLanguages(final Collection<String> languages) {
		this.languages = languages;
	}

	@Range(min = 1, max = 24)
	public int getTimeCachedResults() {
		return this.timeCachedResults;
	}

	public void setTimeCachedResults(final int timeCachedResults) {
		this.timeCachedResults = timeCachedResults;
	}

	@Range(min = 1, max = 100)
	public int getMaxNumberResults() {
		return this.maxNumberResults;
	}

	public void setMaxNumberResults(final int maxNumberResults) {
		this.maxNumberResults = maxNumberResults;
	}

	@NotNull
	@NotEmpty
	@ElementCollection
	public Collection<String> getPriorities() {
		return this.priorities;
	}

	public void setPriorities(final Collection<String> priorities) {
		this.priorities = priorities;
	}

	@NotNull
	@ElementCollection
	public Collection<String> getSpamWords() {
		return this.spamWords;
	}

	public void setSpamWords(final Collection<String> spamWords) {
		this.spamWords = spamWords;
	}

	@NotNull
	@ElementCollection
	public Collection<String> getPositiveWords() {
		return this.positiveWords;
	}

	public void setPositiveWords(final Collection<String> positiveWords) {
		this.positiveWords = positiveWords;
	}

	@NotNull
	@ElementCollection
	public Collection<String> getNegativeWords() {
		return this.negativeWords;
	}

	public void setNegativeWords(final Collection<String> negativeWords) {
		this.negativeWords = negativeWords;
	}

	@Min(1)
	public int getRowLimit() {
		return this.rowLimit;
	}

	public void setRowLimit(final int rowLimit) {
		this.rowLimit = rowLimit;
	}

	@Min(1)
	public int getColumnLimit() {
		return this.columnLimit;
	}

	public void setColumnLimit(final int columnLimit) {
		this.columnLimit = columnLimit;
	}

}
