package pt.oofaround.util;

import com.google.gson.JsonArray;

public class RouteData {

	public String name;
	public String description;
	public String creatorUsername;
	public JsonArray locationNames;
	public String tokenID;
	public String role;
	public String usernameR;

	public RouteData() {
	}

	public RouteData(String name, String description, String creatorUsername, JsonArray locationNames, String tokenID, String role, String usernameR) {
		this.name = name;
		this.description = description;
		this.creatorUsername = creatorUsername;
		this.locationNames = locationNames;
		this.tokenID = tokenID;
		this.role = role;
		this.usernameR = usernameR;
	}
	
	
	
}
