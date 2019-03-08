
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class LinkRecord extends Record {

	// Constructor

	public LinkRecord() {
		super();
	}


	// Attributes

	private String	linkBrotherhood;


	@NotBlank
	@URL
	@SafeHtml(whitelistType = SafeHtml.WhiteListType.NONE)
	public String getLinkBrotherhood() {
		return this.linkBrotherhood;
	}

	public void setLinkBrotherhood(final String linkBrotherhood) {
		this.linkBrotherhood = linkBrotherhood;
	}

}
