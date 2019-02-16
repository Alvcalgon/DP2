
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Box extends DomainEntity {

	// Constructor

	public Box() {
		super();
	}


	// Attributes

	private String	name;
	private boolean	isSystemBox;


	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public boolean isSystemBox() {
		return this.isSystemBox;
	}

	public void setSystemBox(final boolean isSystemBox) {
		this.isSystemBox = isSystemBox;
	}


	//Relationships ----------------------------------------------------

	private Actor	actor;
	private Box		parentBox;


	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Actor getActor() {
		return this.actor;
	}

	public void setActor(final Actor actor) {
		this.actor = actor;
	}

	@Valid
	@ManyToOne(optional = true)
	public Box getParentBox() {
		return this.parentBox;
	}

	public void setParentBox(final Box parentBox) {
		this.parentBox = parentBox;
	}

}
