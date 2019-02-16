
package domain;

import java.util.Date;

import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;

public class Enrolment extends DomainEntity {

	// Constructor

	public Enrolment() {
		super();
	}


	// Attributes

	private Date	registeredMoment;
	private String	position;
	private Date	dropOutMoment;


	@Past
	public Date getRegisteredMoment() {
		return this.registeredMoment;
	}

	public void setRegisteredMoment(final Date registeredMoment) {
		this.registeredMoment = registeredMoment;
	}

	@NotBlank
	public String getPosition() {
		return this.position;
	}

	public void setPosition(final String position) {
		this.position = position;
	}

	@Past
	public Date getDropOutMoment() {
		return this.dropOutMoment;
	}

	public void setDropOutMoment(final Date dropOutMoment) {
		this.dropOutMoment = dropOutMoment;
	}

}
