
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Segment extends DomainEntity {

	// Constructor

	public Segment() {
		super();
	}


	// Attributes

	private GPSCoordinates	origin;
	private GPSCoordinates	destination;
	private Date			reachingOrigin;
	private Date			reachingDestination;


	public GPSCoordinates getOrigin() {
		return this.origin;
	}

	public void setOrigin(final GPSCoordinates origin) {
		this.origin = origin;
	}

	public GPSCoordinates getDestination() {
		return this.destination;
	}

	public void setDestination(final GPSCoordinates destination) {
		this.destination = destination;
	}

	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getReachingOrigin() {
		return this.reachingOrigin;
	}

	public void setReachingOrigin(final Date reachingOrigin) {
		this.reachingOrigin = reachingOrigin;
	}

	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getReachingDestination() {
		return this.reachingDestination;
	}

	public void setReachingDestination(final Date reachingDestination) {
		this.reachingDestination = reachingDestination;
	}

}
