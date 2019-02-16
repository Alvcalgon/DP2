
package domain;

import java.util.Date;

import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;

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


	@Past
	public Date getSentMoment() {
		return this.sentMoment;
	}

	public void setSentMoment(final Date sentMoment) {
		this.sentMoment = sentMoment;
	}

	@NotBlank
	public String getSubject() {
		return this.subject;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

	@NotBlank
	public String getBody() {
		return this.body;
	}

	public void setBody(final String body) {
		this.body = body;
	}

	@NotBlank
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
