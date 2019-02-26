
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
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
@Table(uniqueConstraints = {
	@UniqueConstraint(columnNames = {
		"procession", "member"
	})
})
public class Request extends DomainEntity {

	// Constructor

	public Request() {
		super();
	}


	// Attributes

	private String	status;
	private Integer	rowProcession;
	private Integer	columnProcession;
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
	public Integer getRowProcession() {
		return this.rowProcession;
	}

	public void setRowProcession(final Integer rowProcession) {
		this.rowProcession = rowProcession;
	}

	@Min(1)
	public Integer getColumnProcession() {
		return this.columnProcession;
	}

	public void setColumnProcession(final Integer columnProcession) {
		this.columnProcession = columnProcession;
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
	private Procession	procession;


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
	public Procession getProcession() {
		return this.procession;
	}

	public void setProcession(final Procession procession) {
		this.procession = procession;
	}

}
