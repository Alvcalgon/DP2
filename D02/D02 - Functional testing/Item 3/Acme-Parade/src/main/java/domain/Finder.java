
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "keyword, area, minimumDate, maximumDate"),
})
public class Finder extends DomainEntity {

	// Constructor

	public Finder() {
		super();
	}


	// Attributes

	private String	keyword;
	private String	area;
	private Date	minimumDate;
	private Date	maximumDate;
	private Date	updatedMoment;


	@NotNull
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getKeyword() {
		return this.keyword;
	}

	public void setKeyword(final String keyword) {
		this.keyword = keyword;
	}

	@NotNull
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getArea() {
		return this.area;
	}

	public void setArea(final String area) {
		this.area = area;
	}

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getMinimumDate() {
		return this.minimumDate;
	}

	public void setMinimumDate(final Date minimumDate) {
		this.minimumDate = minimumDate;
	}

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getMaximumDate() {
		return this.maximumDate;
	}

	public void setMaximumDate(final Date maximumDate) {
		this.maximumDate = maximumDate;
	}

	@Past
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getUpdatedMoment() {
		return this.updatedMoment;
	}

	public void setUpdatedMoment(final Date updatedMoment) {
		this.updatedMoment = updatedMoment;
	}


	//Relationships ----------------------------------------------------

	private Member				member;
	private Collection<Parade>	parades;


	@Valid
	@NotNull
	@OneToOne(optional = false)
	public Member getMember() {
		return this.member;
	}

	public void setMember(final Member member) {
		this.member = member;
	}

	@NotNull
	@ManyToMany
	public Collection<Parade> getParades() {
		return this.parades;
	}

	public void setParades(final Collection<Parade> parades) {
		this.parades = parades;
	}

}
