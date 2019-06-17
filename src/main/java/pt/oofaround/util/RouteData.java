package pt.oofaround.util;

public class RouteData {

	public String name;
	public String description;
	public String creatorUsername;
	public CreateRouteData[] locationNames;
	public String tokenID;
	public String role;
	public String usernameR;
	public String rating;

	public RouteData() {
	}

	public RouteData(String name, String description, String creatorUsername, String tokenID, String role,
			String usernameR, CreateRouteData[] locationNames) {
		this.name = name;
		this.description = description;
		this.creatorUsername = creatorUsername;
		this.locationNames = locationNames;
		this.tokenID = tokenID;
		this.role = role;
		this.usernameR = usernameR;
	}

	public RouteData(String name, String tokenID, String role, String usernameR, String rating) {
		this.name = name;
		this.tokenID = tokenID;
		this.role = role;
		this.usernameR = usernameR;
		this.rating = rating;
	}

}
