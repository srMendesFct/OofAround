package pt.oofaround.util;

public class LocationData {

	public String tokenID;
	public String usernameR;
	public String role;
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
	// Open hours and cupons later

	public LocationData() {
	}

	public LocationData(String tokenID, String usernameR, String role, String name, String description, String address,
			String category, String longitude, String latitude, byte[] image, String limit, String lastRequest,
			String lastName, String region) {
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
	}

	public LocationData(String tokenID, String usernameR, String role, String name) {
		this.tokenID = tokenID;
		this.usernameR = usernameR;
		this.role = role;
		this.name = name;
	}

	public LocationData(String tokenID, String usernameR, String role, int lastRequest, int limit, String lastName,
			String category) {
		this.tokenID = tokenID;
		this.usernameR = usernameR;
		this.role = role;
		this.limit = limit;
		this.lastName = lastName;
		this.category = category;
	}

	public LocationData(String tokenID, String usernameR, String role, int limit, String lastName,
			String category, String region) {
		this.tokenID = tokenID;
		this.usernameR = usernameR;
		this.role = role;
		this.limit = limit;
		this.lastName = lastName;
		this.category = category;
		this.region = region;
	}

}
