
package domain;

public class SocialProfile extends DomainEntity {

	// Constructor

	public SocialProfile() {
		super();
	}


	// Attributes

	private String	nick;
	private String	socialNetwork;
	private String	linkProfile;


	public String getNick() {
		return this.nick;
	}

	public void setNick(final String nick) {
		this.nick = nick;
	}

	public String getSocialNetwork() {
		return this.socialNetwork;
	}

	public void setSocialNetwork(final String socialNetwork) {
		this.socialNetwork = socialNetwork;
	}

	public String getLinkProfile() {
		return this.linkProfile;
	}

	public void setLinkProfile(final String linkProfile) {
		this.linkProfile = linkProfile;
	}

}
