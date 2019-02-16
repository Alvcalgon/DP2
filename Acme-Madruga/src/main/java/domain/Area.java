
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
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


	//Relationships ----------------------------------------------------

	private Collection<Brotherhood>	brotherhoods;


	@NotNull
	@OneToMany(mappedBy = "area")
	public Collection<Brotherhood> getBrotherhoods() {
		return this.brotherhoods;
	}

	public void setBrotherhoods(final Collection<Brotherhood> brotherhoods) {
		this.brotherhoods = brotherhoods;
	}

}
