
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;

@Embeddable
@Access(AccessType.PROPERTY)
public class CreditCard {

	private String	holder;
	private String	make;
	private String	number;
	private String	expirationMonth;
	private String	expirationYear;
	private int		cvvCode;


	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getHolder() {
		return this.holder;
	}

	public void setHolder(final String holder) {
		this.holder = holder;
	}

	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getMake() {
		return this.make;
	}

	public void setMake(final String make) {
		this.make = make;
	}

	@NotBlank
	@CreditCardNumber
	@Pattern(regexp = "\\d+")
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getNumber() {
		return this.number;
	}

	public void setNumber(final String number) {
		this.number = number;
	}

	@NotBlank
	@Range(min = 1, max = 12)
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getExpirationMonth() {
		return this.expirationMonth;
	}

	public void setExpirationMonth(final String expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	@NotBlank
	@Digits(integer = 2, fraction = 0)
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getExpirationYear() {
		return this.expirationYear;
	}

	public void setExpirationYear(final String expirationYear) {
		this.expirationYear = expirationYear;
	}

	@Range(min = 100, max = 999)
	public int getCvvCode() {
		return this.cvvCode;
	}

	public void setCvvCode(final int cvvCode) {
		this.cvvCode = cvvCode;
	}

}
