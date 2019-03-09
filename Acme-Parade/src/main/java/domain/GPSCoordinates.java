
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

@Embeddable
@Access(AccessType.PROPERTY)
public class GPSCoordinates {

	// Attributes

	private Double	latitude;
	private Double	longitude;


	@Digits(integer = 8, fraction = 6)
	@Range(min = -90, max = 90)
	@NotNull
	public Double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(final Double latitude) {
		this.latitude = latitude;
	}

	@Digits(integer = 9, fraction = 6)
	@Range(min = -180, max = 180)
	@NotNull
	public Double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(final Double longitude) {
		this.longitude = longitude;
	}

}
