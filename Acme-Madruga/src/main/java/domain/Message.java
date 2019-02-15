
package domain;

import java.util.Date;

public class Message extends DomainEntity {

	// Constructor

	public Message() {
		super();
	}


	// Attributes

	private Date	sentMoment;
	private String	subject;
	private String	body;
	private String	priority;
	private String	tags;


	public Date getSentMoment() {
		return this.sentMoment;
	}

	public void setSentMoment(final Date sentMoment) {
		this.sentMoment = sentMoment;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return this.body;
	}

	public void setBody(final String body) {
		this.body = body;
	}

	public String getPriority() {
		return this.priority;
	}

	public void setPriority(final String priority) {
		this.priority = priority;
	}

	public String getTags() {
		return this.tags;
	}

	public void setTags(final String tags) {
		this.tags = tags;
	}

}
