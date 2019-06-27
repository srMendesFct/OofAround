package pt.oofaround.util;

public class CuponData {

	public String locationName;
	public String latitude;
	public String longitude;
	public double value;
	public String tokenID;
	public String usernameR;
	public String role;
	public String description;
	public String region;

	public CuponData() {
	}

	public CuponData(String locationName, String latitude, String longitude, double value, String tokenID,
			String usernameR, String role, String description, String region) {
		this.locationName = locationName;
		this.latitude = latitude;
		this.longitude = longitude;
		this.value = value;
		this.tokenID = tokenID;
		this.usernameR = usernameR;
		this.role = role;
		this.description = description;
		this.region = region;
	}

}
