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
	public String[] categories;
	public String region;
	public long score;

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

	public RouteData(String name, String tokenID, String role, String usernameR, int rating, String creatorUsername) {
		this.name = name;
		this.tokenID = tokenID;
		this.role = role;
		this.usernameR = usernameR;
		this.rating = String.valueOf(rating);
		this.creatorUsername = creatorUsername;
	}

	public RouteData(String name, String tokenID, String role, String usernameR, String creatorUsername) {
		this.name = name;
		this.tokenID = tokenID;
		this.role = role;
		this.usernameR = usernameR;
		this.creatorUsername = creatorUsername;
	}
	
	public RouteData(String name, String tokenID, String role, String usernameR, String creatorUsername, long score) {
		this.name = name;
		this.tokenID = tokenID;
		this.role = role;
		this.usernameR = usernameR;
		this.creatorUsername = creatorUsername;
		this.score = score;
	}

	public RouteData(String tokenID, String role, String usernameR) {
		this.tokenID = tokenID;
		this.role = role;
		this.usernameR = usernameR;
	}

	public RouteData(String name, String tokenID, String role, String usernameR, String[] categories, String region) {
		this.name = name;
		this.tokenID = tokenID;
		this.role = role;
		this.usernameR = usernameR;
		this.categories = categories;
		this.region = region;
	}

	public RouteData(String tokenID, String role, String usernameR, String[] categories) {
		this.tokenID = tokenID;
		this.role = role;
		this.usernameR = usernameR;
		this.categories = categories;
	}
}
