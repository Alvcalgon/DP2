
package domain;

import java.util.Date;

import javax.persistence.Column;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

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
	private boolean	finalMode;


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

	public Date getMoment() {
		return this.moment;
	}

	public void setMoment(final Date moment) {
		this.moment = moment;
	}

	public boolean isFinalMode() {
		return this.finalMode;
	}

	public void setFinalMode(final boolean finalMode) {
		this.finalMode = finalMode;
	}

}
