
package forms;

import domain.Member;
import domain.Parade;

public class RequestForm {

	private int			id;
	private Integer		rowParade;
	private Integer		columnParade;
	private String		reasonWhy;
	private Parade	parade;
	private String		status;
	private Member		member;
	private Integer		positionParade;


	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public Integer getRowParade() {
		return this.rowParade;
	}
	public void setRowParade(final Integer rowParade) {
		this.rowParade = rowParade;
	}
	public Integer getColumnParade() {
		return this.columnParade;
	}
	public void setColumnParade(final Integer columnParade) {
		this.columnParade = columnParade;
	}
	public String getReasonWhy() {
		return this.reasonWhy;
	}
	public void setReasonWhy(final String reasonWhy) {
		this.reasonWhy = reasonWhy;
	}

	public Parade getParade() {
		return this.parade;
	}

	public void setParade(final Parade parade) {
		this.parade = parade;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public Member getMember() {
		return this.member;
	}

	public void setMember(final Member member) {
		this.member = member;
	}

	public Integer getPositionParade() {
		return this.positionParade;
	}

	public void setPositionParade(final Integer positionParade) {
		this.positionParade = positionParade;
	}

}
