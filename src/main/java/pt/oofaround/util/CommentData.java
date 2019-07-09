package pt.oofaround.util;

public class CommentData {

	public String role;
	public String tokenID;
	public String usernameR;
	public String comment;
	public String routeName;
	public String routeCreatorUsername;

	public CommentData() {
	}

	public CommentData(String role, String tokenID, String usernameR, String comment, String routeName,
			String routeCreatorUsername) {
		this.role = role;
		this.tokenID = tokenID;
		this.usernameR = usernameR;
		this.comment = comment;
		this.routeName = routeName;
		this.routeCreatorUsername = routeCreatorUsername;
	}

	public CommentData(String role, String tokenID, String usernameR, String routeName, String routeCreatorUsername) {
		this.role = role;
		this.tokenID = tokenID;
		this.usernameR = usernameR;
		this.routeName = routeName;
		this.routeCreatorUsername = routeCreatorUsername;
	}

}
