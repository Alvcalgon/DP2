
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;

@Embeddable
@Access(AccessType.PROPERTY)
public class GPSCoordinates {

	// Attributes

	private double	latitude;
	private double	longitude;


	@Digits(integer = 8, fraction = 6)
	@DecimalMax("90.0")
	@DecimalMin("-90.0")
	public double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(final double latitude) {
		this.latitude = latitude;
	}

	@Digits(integer = 9, fraction = 6)
	@DecimalMax("180.0")
	@DecimalMin("-180.0")
	public double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(final double longitude) {
		this.longitude = longitude;
	}

}
