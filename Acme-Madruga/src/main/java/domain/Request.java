
package domain;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

public class Request extends DomainEntity {

	// Constructor

	public Request() {
		super();
	}


	// Attributes

	private String	status;
	private Integer	row;
	private Integer	column;
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
	public Integer getRow() {
		return this.row;
	}

	public void setRow(final Integer row) {
		this.row = row;
	}

	@Min(1)
	public Integer getColumn() {
		return this.column;
	}

	public void setColumn(final Integer column) {
		this.column = column;
	}

	public String getReasonWhy() {
		return this.reasonWhy;
	}

	public void setReasonWhy(final String reasonWhy) {
		this.reasonWhy = reasonWhy;
	}

}
