
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Access(AccessType.PROPERTY)
public class Position extends DomainEntity {

	// Constructor

	public Position() {
		super();
	}


	// Attributes

	//Relationships ----------------------------------------------------

	private Collection<TranslationPosition>	translationPositions;


	@NotEmpty
	@NotNull
	@OneToMany
	public Collection<TranslationPosition> getTranslationPositions() {
		return this.translationPositions;
	}

	public void setTranslationPositions(final Collection<TranslationPosition> translationPositions) {
		this.translationPositions = translationPositions;
	}

}
