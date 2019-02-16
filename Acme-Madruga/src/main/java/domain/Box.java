
package domain;

import org.hibernate.validator.constraints.NotBlank;

public class Box extends DomainEntity {

	// Constructor

	public Box() {
		super();
	}


	// Attributes

	private String	name;
	private boolean	isSystemBox;


	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public boolean isSystemBox() {
		return this.isSystemBox;
	}

	public void setSystemBox(final boolean isSystemBox) {
		this.isSystemBox = isSystemBox;
	}

}
