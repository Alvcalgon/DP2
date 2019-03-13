
package forms;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

public class SegmentForm {

	int				id;
	private double	originLatitude;
	private double	originLongitude;
	private double	destinationLatitude;
	private double	destinationLongitude;
	Date			reachingOrigin;
	Date			reachingDestination;
	int				paradeId;


	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public int getParadeId() {
		return this.paradeId;
	}

	public void setParadeId(final int paradeId) {
		this.paradeId = paradeId;
	}

	public double getOriginLatitude() {
		return this.originLatitude;
	}

	public void setOriginLatitude(final double originLatitude) {
		this.originLatitude = originLatitude;
	}

	public double getOriginLongitude() {
		return this.originLongitude;
	}

	public void setOriginLongitude(final double originLongitude) {
		this.originLongitude = originLongitude;
	}

	public double getDestinationLatitude() {
		return this.destinationLatitude;
	}

	public void setDestinationLatitude(final double destinationLatitude) {
		this.destinationLatitude = destinationLatitude;
	}

	public double getDestinationLongitude() {
		return this.destinationLongitude;
	}

	public void setDestinationLongitude(final double destinationLongitude) {
		this.destinationLongitude = destinationLongitude;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getReachingOrigin() {
		return this.reachingOrigin;
	}

	public void setReachingOrigin(final Date reachingOrigin) {
		this.reachingOrigin = reachingOrigin;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getReachingDestination() {
		return this.reachingDestination;
	}

	public void setReachingDestination(final Date reachingDestination) {
		this.reachingDestination = reachingDestination;
	}

}
