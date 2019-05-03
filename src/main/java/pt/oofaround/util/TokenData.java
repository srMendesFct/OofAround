package pt.oofaround.util;

public class TokenData {

	public String tokenID;
	public String usernameR;
	public String role;
	public String action;
	public long expirationDate;

	public TokenData(String tokenID, String usernameR, String role, String action, long expirationDate) {
		this.tokenID = tokenID;
		this.usernameR = usernameR;
		this.role = role;
		this.action = action;
		this.expirationDate = expirationDate;
	}

}
