
package forms;


public class SocialProfileForm {

	private int		id;
	private String	nick;
	private String	socialNetwork;
	private String	linkProfile;


	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

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
