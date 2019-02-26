
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {
	"name", "parent"
}))
public class Box extends DomainEntity {

	// Constructor

	public Box() {
		super();
	}


	// Attributes

	private String	name;
	private boolean	isSystemBox;


	@NotBlank
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public boolean getIsSystemBox() {
		return this.isSystemBox;
	}

	public void setIsSystemBox(final boolean isSystemBox) {
		this.isSystemBox = isSystemBox;
	}


	//Relationships ----------------------------------------------------

	private Actor				actor;
	private Box					parent;
	private Collection<Message>	messages;


	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Actor getActor() {
		return this.actor;
	}

	public void setActor(final Actor actor) {
		this.actor = actor;
	}

	@Valid
	@ManyToOne(optional = true)
	public Box getParent() {
		return this.parent;
	}

	public void setParent(final Box parent) {
		this.parent = parent;
	}

	@NotNull
	@ManyToMany
	public Collection<Message> getMessages() {
		return this.messages;
	}

	public void setMessages(final Collection<Message> messages) {
		this.messages = messages;
	}

}
