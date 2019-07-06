package pt.oofaround.util;

public class ChangePasswordData {

	public String password;
	public String oldPassword;
	public String tokenID;
	public String role;
	public String usernameR;
	public String confirmPassword;

	public ChangePasswordData() {
	}

	public ChangePasswordData(String password, String tokenID, String role, String usernameR, String oldPassword,
			String confirmPassword) {
		this.password = password;
		this.oldPassword = oldPassword;
		this.tokenID = tokenID;
		this.role = role;
		this.usernameR = usernameR;
		this.confirmPassword = confirmPassword;
	}

	public ChangePasswordData(String tokenID, String role, String usernameR) {
		this.tokenID = tokenID;
		this.role = role;
		this.usernameR = usernameR;
	}
}
