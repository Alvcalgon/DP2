
package domain;

import java.util.Date;

import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;

public class Brotherhood extends Actor {

	// Constructor

	public Brotherhood() {
		super();
	}


	// Attributes

	private String	title;
	private Date	establishmentDate;
	private String	pictures;


	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@Past
	public Date getEstablishmentDate() {
		return this.establishmentDate;
	}

	public void setEstablishmentDate(final Date establishmentDate) {
		this.establishmentDate = establishmentDate;
	}

	public String getPictures() {
		return this.pictures;
	}

	public void setPictures(final String pictures) {
		this.pictures = pictures;
	}

}
