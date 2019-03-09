
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
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

	private String	name;
	private String	banner;
	private String	spanishWelcomeMessage;
	private String	englishWelcomeMessage;
	private String	countryCode;
	private String	languages;
	private int		timeCachedResults;
	private int		maxNumberResults;
	private String	priorities;
	private String	spamWords;
	private String	positiveWords;
	private String	negativeWords;
	private int		rowLimit;
	private int		columnLimit;
	private double	thresholdScore;
	private String	creditCardMakes;
	private Double	vatPercentage;
	private Double	fare;


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
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
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

	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getLanguages() {
		return this.languages;
	}

	public void setLanguages(final String languages) {
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

	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getPriorities() {
		return this.priorities;
	}

	public void setPriorities(final String priorities) {
		this.priorities = priorities;
	}

	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getSpamWords() {
		return this.spamWords;
	}

	public void setSpamWords(final String spamWords) {
		this.spamWords = spamWords;
	}

	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getPositiveWords() {
		return this.positiveWords;
	}

	public void setPositiveWords(final String positiveWords) {
		this.positiveWords = positiveWords;
	}

	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getNegativeWords() {
		return this.negativeWords;
	}

	public void setNegativeWords(final String negativeWords) {
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

	@Range(min = -1, max = 0)
	@Digits(integer = 3, fraction = 2)
	public double getThresholdScore() {
		return this.thresholdScore;
	}

	public void setThresholdScore(final double thresholdScore) {
		this.thresholdScore = thresholdScore;
	}

	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	@NotNull
	public String getCreditCardMakes() {
		return this.creditCardMakes;
	}

	public void setCreditCardMakes(final String creditCardMakes) {
		this.creditCardMakes = creditCardMakes;
	}

	@Range(min = 0, max = 1)
	@Digits(integer = 3, fraction = 2)
	@NotNull
	public Double getVatPercentage() {
		return this.vatPercentage;
	}

	public void setVatPercentage(final Double vatPercentage) {
		this.vatPercentage = vatPercentage;
	}

	@Digits(integer = 9, fraction = 2)
	@Min(0)
	@NotNull
	public Double getFare() {
		return this.fare;
	}

	public void setFare(final Double fare) {
		this.fare = fare;
	}

}
