
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Procession extends DomainEntity {

	// Constructor

	public Procession() {
		super();
	}


	// Attributes

	private String	ticker;
	private String	title;
	private String	description;
	private Date	moment;
	private boolean	isFinalMode;
	private int[][]	matrizProcession;


	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "\\d{6}-[A-Z]{5}")
	public String getTicker() {
		return this.ticker;
	}

	public void setTicker(final String ticker) {
		this.ticker = ticker;
	}

	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@Future
	public Date getMoment() {
		return this.moment;
	}

	public void setMoment(final Date moment) {
		this.moment = moment;
	}

	public boolean getIsFinalMode() {
		return this.isFinalMode;
	}

	public void setIsFinalMode(final boolean isFinalMode) {
		this.isFinalMode = isFinalMode;
	}

	public int[][] getMatrizProcession() {
		return this.matrizProcession;
	}

	public void setMatrizProcession(final int[][] matrizProcession) {
		this.matrizProcession = matrizProcession;
	}


	//Relationships ----------------------------------------------------

	private Collection<Float>	floats;


	@NotNull
	@NotEmpty
	@ManyToMany
	public Collection<Float> getFloats() {
		return this.floats;
	}

	public void setFloats(final Collection<Float> floats) {
		this.floats = floats;
	}
}
