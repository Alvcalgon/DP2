
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class LegalRecord extends Record {

	// Constructor

	public LegalRecord() {
		super();
	}


	// Attributes

	private String	name;
	private int		vatNumber;
	private String	laws;


	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Range(min = 0, max = 100)
	public int getVatNumber() {
		return this.vatNumber;
	}

	public void setVatNumber(final int vatNumber) {
		this.vatNumber = vatNumber;
	}

	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getLaws() {
		return this.laws;
	}

	public void setLaws(final String laws) {
		this.laws = laws;
	}

}
