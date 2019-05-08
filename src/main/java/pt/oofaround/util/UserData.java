package pt.oofaround.util;

public class UserData {

	public String username;
	public String tokenID;
	public String usernameR;
	public String role;
	public String action;
	public long expirationDate;

	public UserData(String username, String tokenID, String role, String action, long expirationDate) {
		this.username = username;
		this.tokenID = tokenID;
		this.role = role;
		this.action = action;
		this.expirationDate = expirationDate;
		this.usernameR = username;
	}

	public UserData(String username, String tokenID, String usernameR, String role, String action,
			long expirationDate) {
		this.username = username;
		this.tokenID = tokenID;
		this.usernameR = usernameR;
		this.role = role;
		this.action = action;
		this.expirationDate = expirationDate;
	}

}
