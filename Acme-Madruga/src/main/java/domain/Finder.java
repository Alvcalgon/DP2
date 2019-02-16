
package domain;

import java.util.Date;

import javax.validation.constraints.Past;

public class Finder extends DomainEntity {

	// Constructor

	public Finder() {
		super();
	}


	// Attributes

	private String	keyword;
	private String	area;
	private Date	minimumDate;
	private Date	maximumDate;
	private Date	updatedMoment;


	public String getKeyword() {
		return this.keyword;
	}

	public void setKeyword(final String keyword) {
		this.keyword = keyword;
	}

	public String getArea() {
		return this.area;
	}

	public void setArea(final String area) {
		this.area = area;
	}

	public Date getMinimumDate() {
		return this.minimumDate;
	}

	public void setMinimumDate(final Date minimumDate) {
		this.minimumDate = minimumDate;
	}

	public Date getMaximumDate() {
		return this.maximumDate;
	}

	public void setMaximumDate(final Date maximumDate) {
		this.maximumDate = maximumDate;
	}

	@Past
	public Date getUpdatedMoment() {
		return this.updatedMoment;
	}

	public void setUpdatedMoment(final Date updatedMoment) {
		this.updatedMoment = updatedMoment;
	}

}
