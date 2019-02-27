
package forms;

import domain.Member;
import domain.Procession;

public class RequestForm {

	private int			id;
	private Integer		rowProcession;
	private Integer		columnProcession;
	private String		reasonWhy;
	private Procession	procession;
	private String		status;
	private Member		member;


	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public Integer getRowProcession() {
		return this.rowProcession;
	}
	public void setRowProcession(final Integer rowProcession) {
		this.rowProcession = rowProcession;
	}
	public Integer getColumnProcession() {
		return this.columnProcession;
	}
	public void setColumnProcession(final Integer columnProcession) {
		this.columnProcession = columnProcession;
	}
	public String getReasonWhy() {
		return this.reasonWhy;
	}
	public void setReasonWhy(final String reasonWhy) {
		this.reasonWhy = reasonWhy;
	}

	public Procession getProcession() {
		return this.procession;
	}

	public void setProcession(final Procession procession) {
		this.procession = procession;
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

}
