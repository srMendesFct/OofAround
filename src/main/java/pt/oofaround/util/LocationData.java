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
	// Open hours and cupons later

	public LocationData() {
	}

	public LocationData(String tokenID, String usernameR, String role, String name, String description, String address,
			String category, String longitude, String latitude, byte[] image) {
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
	}

	public LocationData(String tokenID, String usernameR, String role, String name) {
		this.tokenID = tokenID;
		this.usernameR = usernameR;
		this.role = role;
		this.name = name;
	}

}
