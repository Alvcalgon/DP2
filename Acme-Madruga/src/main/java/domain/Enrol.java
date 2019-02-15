
package domain;

import java.util.Date;

public class Enrol extends DomainEntity {

	// Constructor

	public Enrol() {
		super();
	}


	// Attributes

	private Date	registeredMoment;
	private String	position;
	private Date	dropOutMoment;


	public Date getRegisteredMoment() {
		return this.registeredMoment;
	}

	public void setRegisteredMoment(final Date registeredMoment) {
		this.registeredMoment = registeredMoment;
	}

	public String getPosition() {
		return this.position;
	}

	public void setPosition(final String position) {
		this.position = position;
	}

	public Date getDropOutMoment() {
		return this.dropOutMoment;
	}

	public void setDropOutMoment(final Date dropOutMoment) {
		this.dropOutMoment = dropOutMoment;
	}

}
