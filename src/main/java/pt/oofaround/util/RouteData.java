package pt.oofaround.util;

public class RouteData {

	public String name;
	public String description;
	public String creatorUsername;
	public String[] locationNames;
	public String[] categories; //possivelmente fazer na resource
	public float rating;
	public String status;
	public String tokenID;
	public String role;
	public String usernameR;

	public RouteData() {
	}

	public RouteData(String name, String description, String creatorUsername, String[] locationNames,
			String[] categories, float rating, String status, String tokenID, String role, String usernameR) {
		this.name = name;
		this.description = description;
		this.creatorUsername = creatorUsername;
		this.locationNames = locationNames;
		this.categories = categories;
		this.rating = rating;
		this.status = status;
		this.tokenID = tokenID;
		this.role = role;
		this.usernameR = usernameR;
	}
	
	
	
}
