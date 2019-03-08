
package forms;

public class MessageForm {

	private int		messageId;
	private int		originBoxId;
	private Integer	destinationBoxId;


	public int getMessageId() {
		return this.messageId;
	}

	public void setMessageId(final int messageId) {
		this.messageId = messageId;
	}

	public int getOriginBoxId() {
		return this.originBoxId;
	}

	public void setOriginBoxId(final int originBoxId) {
		this.originBoxId = originBoxId;
	}

	public Integer getDestinationBoxId() {
		return this.destinationBoxId;
	}

	public void setDestinationBoxId(final Integer destinationBoxId) {
		this.destinationBoxId = destinationBoxId;
	}

}
