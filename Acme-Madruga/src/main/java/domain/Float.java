
package domain;

public class Float extends DomainEntity {

	// Constructor

	public Float() {
		super();
	}


	// Attributes

	private String	name;
	private String	description;
	private String	pictures;


	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getPictures() {
		return this.pictures;
	}

	public void setPictures(final String pictures) {
		this.pictures = pictures;
	}

}
