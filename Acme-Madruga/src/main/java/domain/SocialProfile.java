
package domain;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public class SocialProfile extends DomainEntity {

	// Constructor

	public SocialProfile() {
		super();
	}


	// Attributes

	private String	nick;
	private String	socialNetwork;
	private String	linkProfile;


	@NotBlank
	public String getNick() {
		return this.nick;
	}

	public void setNick(final String nick) {
		this.nick = nick;
	}

	@NotBlank
	public String getSocialNetwork() {
		return this.socialNetwork;
	}

	public void setSocialNetwork(final String socialNetwork) {
		this.socialNetwork = socialNetwork;
	}

	@NotBlank
	@URL
	public String getLinkProfile() {
		return this.linkProfile;
	}

	public void setLinkProfile(final String linkProfile) {
		this.linkProfile = linkProfile;
	}

}
