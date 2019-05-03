package pt.oofaround.util;

public class DeleteData {
	public String username;
	public String tokenID;
	public String role;
	public String usernameR;
	public long expirationDate;

	public DeleteData() {
	}

	public DeleteData(String username, String tokenID, String role, String usernameR, long expirationDate) {
		this.username = username;
		this.tokenID = tokenID;
		this.role = role;
		this.usernameR = usernameR;
		this.expirationDate = expirationDate;
	}

}
