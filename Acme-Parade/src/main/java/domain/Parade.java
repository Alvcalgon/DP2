
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "isFinalMode, ticker, title, description, moment")
})
public class Parade extends DomainEntity {

	// Constructor

	public Parade() {
		super();
	}


	// Attributes

	private String		ticker;
	private String		title;
	private String		description;
	private Date		moment;
	private boolean		isFinalMode;
	private Integer[][]	matrizParade;


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
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
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

	@Column(length = 400000000)
	public Integer[][] getMatrizParade() {
		return this.matrizParade;
	}

	public void setMatrizParade(final Integer[][] matrizParade) {
		this.matrizParade = matrizParade;
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
