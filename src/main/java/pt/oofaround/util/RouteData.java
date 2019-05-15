package pt.oofaround.util;

public class RouteData {

	public String name;
	public String description;
	public String creatorUsername;
	public String[] locationNames;
	public String[] categories; //possivelmente fazer na resource
	public float rating;
	public String status;

	public RouteData() {
	}
	
	public RouteData(String name, String description, String creatorUsername, String[] locationNames,
			String[] categories, float rating, String status) {
		this.name = name;
		this.description = description;
		this.creatorUsername = creatorUsername;
		this.locationNames = locationNames;
		this.categories = categories;
		this.rating = rating;
		this.status = status;
	}
	
}
