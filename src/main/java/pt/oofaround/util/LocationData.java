package pt.oofaround.util;

public class LocationData {

	public String tokenID;
	public String usernameR;
	public String role;
	public String placeID;
	public String name;
	public String description;
	public String address;
	public String latitude;
	public String longitude;
	public String category;
	public byte[] image;
	public int limit;
	public String lastName;
	public String region;
	public long score;
	// Open hours and cupons later

	public LocationData() {
	}

	// for creating
	public LocationData(String tokenID, String usernameR, String role, String name, String description, String address,
			String category, String longitude, String latitude, byte[] image, String limit, String lastRequest,
			String lastName, String region, String score) {
		this.tokenID = tokenID;
		this.usernameR = usernameR;
		this.role = role;
		this.name = name;
		this.description = description;
		this.address = address;
		this.category = category;
		this.latitude = latitude;
		this.longitude = longitude;
		this.image = image;
		this.region = region;
		this.score = Long.valueOf(score);
	}	

	// paginated/collective get
	public LocationData(String tokenID, String usernameR, String role, int limit, String lastName, String category,
			String region) {
		this.tokenID = tokenID;
		this.usernameR = usernameR;
		this.role = role;
		this.limit = limit;
		this.lastName = lastName;
		this.category = category;
		this.region = region;
	}
	
	// for individual get
	public LocationData(String tokenID, String usernameR, String role, String latitude, String longitude) {
		this.tokenID = tokenID;
		this.usernameR = usernameR;
		this.role = role;
		this.latitude = latitude;
		this.longitude = longitude;
	}

}
