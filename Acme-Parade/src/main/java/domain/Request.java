
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "status")
}, uniqueConstraints = {
	@UniqueConstraint(columnNames = {
		"parade", "member"
	})
})
public class Request extends DomainEntity {

	// Constructor

	public Request() {
		super();
	}


	// Attributes

	private String	status;
	private Integer	rowParade;
	private Integer	columnParade;
	private String	reasonWhy;


	@NotBlank
	@Pattern(regexp = "^PENDING|APPROVED|REJECTED$")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	@Min(1)
	public Integer getRowParade() {
		return this.rowParade;
	}

	public void setRowParade(final Integer rowParade) {
		this.rowParade = rowParade;
	}

	@Min(1)
	public Integer getColumnParade() {
		return this.columnParade;
	}

	public void setColumnParade(final Integer columnParade) {
		this.columnParade = columnParade;
	}

	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getReasonWhy() {
		return this.reasonWhy;
	}

	public void setReasonWhy(final String reasonWhy) {
		this.reasonWhy = reasonWhy;
	}


	//Relationships ----------------------------------------------------

	private Member		member;
	private Parade	parade;


	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Member getMember() {
		return this.member;
	}

	public void setMember(final Member member) {
		this.member = member;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Parade getParade() {
		return this.parade;
	}

	public void setParade(final Parade parade) {
		this.parade = parade;
	}

}
