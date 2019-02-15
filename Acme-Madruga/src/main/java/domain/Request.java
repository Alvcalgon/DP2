
package domain;

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


	public String getStatus() {
		return this.status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public Integer getRow() {
		return this.row;
	}

	public void setRow(final Integer row) {
		this.row = row;
	}

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
