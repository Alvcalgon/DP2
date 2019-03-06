
package forms;

import security.UserAccount;

public class RegistrationForm {

	private int			id;
	private String		name;
	private String		middleName;
	private String		surname;
	private String		fullname;
	private String		photo;
	private String		email;
	private String		phoneNumber;
	private String		address;
	private Boolean		isSpammer;
	private Double		score;
	private UserAccount	userAccount;
	private String		username;
	private String		password;
	private String		confirmPassword;
	private boolean		checkBoxAccepted;


	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getMiddleName() {
		return this.middleName;
	}

	public void setMiddleName(final String middleName) {
		this.middleName = middleName;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}

	public String getFullname() {
		return this.fullname;
	}

	public void setFullname(final String fullname) {
		this.fullname = fullname;
	}

	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public Boolean getIsSpammer() {
		return this.isSpammer;
	}

	public void setIsSpammer(final Boolean isSpammer) {
		this.isSpammer = isSpammer;
	}

	public Double getScore() {
		return this.score;
	}

	public void setScore(final Double score) {
		this.score = score;
	}

	public UserAccount getUserAccount() {
		return this.userAccount;
	}

	public void setUserAccount(final UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public String getConfirmPassword() {
		return this.confirmPassword;
	}

	public void setConfirmPassword(final String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public boolean getCheckBoxAccepted() {
		return this.checkBoxAccepted;
	}

	public void setCheckBoxAccepted(final boolean checkBoxAccepted) {
		this.checkBoxAccepted = checkBoxAccepted;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

}
