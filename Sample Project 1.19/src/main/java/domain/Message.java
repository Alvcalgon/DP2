
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
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
	private boolean	isSpam;


	@Past
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getSentMoment() {
		return this.sentMoment;
	}

	public void setSentMoment(final Date sentMoment) {
		this.sentMoment = sentMoment;
	}

	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getSubject() {
		return this.subject;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	@Column(length = 30000)
	public String getBody() {
		return this.body;
	}

	public void setBody(final String body) {
		this.body = body;
	}

	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getPriority() {
		return this.priority;
	}

	public void setPriority(final String priority) {
		this.priority = priority;
	}

	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getTags() {
		return this.tags;
	}

	public void setTags(final String tags) {
		this.tags = tags;
	}

	public boolean getIsSpam() {
		return this.isSpam;
	}

	public void setIsSpam(final boolean isSpam) {
		this.isSpam = isSpam;
	}


	//Relationships ----------------------------------------------------

	private Actor				sender;
	private Collection<Actor>	recipients;


	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Actor getSender() {
		return this.sender;
	}

	public void setSender(final Actor sender) {
		this.sender = sender;
	}

	@NotNull
	@NotEmpty
	@ManyToMany
	public Collection<Actor> getRecipients() {
		return this.recipients;
	}

	public void setRecipients(final Collection<Actor> recipients) {
		this.recipients = recipients;
	}

}
