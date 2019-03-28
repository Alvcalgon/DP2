
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
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


	@Valid
	@NotNull
	@AttributeOverrides({
		@AttributeOverride(name = "latitude", column = @Column(name = "originLatitude")), @AttributeOverride(name = "longitude", column = @Column(name = "originLongitude"))
	})
	public GPSCoordinates getOrigin() {
		return this.origin;
	}

	public void setOrigin(final GPSCoordinates origin) {
		this.origin = origin;
	}

	@Valid
	@NotNull
	@AttributeOverrides({
		@AttributeOverride(name = "latitude", column = @Column(name = "destinationLatitude")), @AttributeOverride(name = "longitude", column = @Column(name = "destinationLongitude"))
	})
	public GPSCoordinates getDestination() {
		return this.destination;
	}

	public void setDestination(final GPSCoordinates destination) {
		this.destination = destination;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getReachingOrigin() {
		return this.reachingOrigin;
	}

	public void setReachingOrigin(final Date reachingOrigin) {
		this.reachingOrigin = reachingOrigin;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getReachingDestination() {
		return this.reachingDestination;
	}

	public void setReachingDestination(final Date reachingDestination) {
		this.reachingDestination = reachingDestination;
	}

}
