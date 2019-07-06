package pt.oofaround.util;

public class RecoverPasswordData {

	public String password;
	public String tokenID;
	public String role;
	public String usernameR;
	public String confirmPassword;
	public String recoverCode;

	public RecoverPasswordData() {
	}

	public RecoverPasswordData(String password, String tokenID, String role, String usernameR, String confirmPassword,
			String recoverCode) {
		this.password = password;
		this.tokenID = tokenID;
		this.role = role;
		this.usernameR = usernameR;
		this.confirmPassword = confirmPassword;
		this.recoverCode = recoverCode;
	}
	
}
