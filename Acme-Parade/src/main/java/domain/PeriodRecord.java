
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class PeriodRecord extends Record {

	// Constructor

	public PeriodRecord() {
		super();
	}


	// Attributes

	private int		startYear;
	private int		endYear;
	private String	photos;


	@Min(1)
	public int getStartYear() {
		return this.startYear;
	}

	public void setStartYear(final int startYear) {
		this.startYear = startYear;
	}

	@Min(1)
	public int getEndYear() {
		return this.endYear;
	}

	public void setEndYear(final int endYear) {
		this.endYear = endYear;
	}

	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getPhotos() {
		return this.photos;
	}

	public void setPhotos(final String photos) {
		this.photos = photos;
	}

}
