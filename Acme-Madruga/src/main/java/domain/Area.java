
package domain;

import org.hibernate.validator.constraints.NotBlank;

public class Area extends DomainEntity {

	// Constructor

	public Area() {
		super();
	}


	// Attributes

	private String	name;
	private String	pictures;


	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getPictures() {
		return this.pictures;
	}

	public void setPictures(final String pictures) {
		this.pictures = pictures;
	}

}
