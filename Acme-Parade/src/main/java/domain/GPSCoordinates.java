
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.validation.constraints.Digits;

import org.hibernate.validator.constraints.Range;

@Embeddable
@Access(AccessType.PROPERTY)
public class GPSCoordinates {

	// Attributes

	private double	latitude;
	private double	longitude;


	@Digits(integer = 8, fraction = 6)
	@Range(min = -90, max = 90)
	public double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(final double latitude) {
		this.latitude = latitude;
	}

	@Digits(integer = 9, fraction = 6)
	@Range(min = -180, max = 180)
	public double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(final double longitude) {
		this.longitude = longitude;
	}

}
