
package forms;


public class RequestForm {

	private int		id;
	private String	reasonWhy;
	private Integer	positionParade;


	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getReasonWhy() {
		return this.reasonWhy;
	}
	public void setReasonWhy(final String reasonWhy) {
		this.reasonWhy = reasonWhy;
	}

	public Integer getPositionParade() {
		return this.positionParade;
	}

	public void setPositionParade(final Integer positionParade) {
		this.positionParade = positionParade;
	}

}
