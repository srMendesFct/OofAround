package pt.oofaround.util;

public class CreateRouteData {

	public String placeId;
	public String name;
	public double latitude;
	public double longitude;
	public String category;
	public String region;
	public int score;

	public CreateRouteData() {
	}

	public CreateRouteData(String region, String placeId, String name, double latitude, double longitude,
			String category) {
		this.placeId = placeId;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.category = category;
		this.region = region;
	}
	
	public CreateRouteData(String region, String placeId, String name, double latitude, double longitude, int score,
			String category) {
		this.placeId = placeId;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.category = category;
		this.region = region;
		this.score = score;
	}

}
