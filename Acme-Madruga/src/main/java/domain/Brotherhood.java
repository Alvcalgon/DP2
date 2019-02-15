
package domain;

import java.util.Date;

public class Brotherhood extends Actor {

	// Constructor

	public Brotherhood() {
		super();
	}


	// Attributes

	private String	title;
	private Date	establishmentDate;
	private String	pictures;


	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

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
