package pt.oofaround.util;

public class GuestData {

	public String region;
	public String[] categories;
	public String lastName;
	public int limit;
	public String name;
	public String creatorUsername;

	public GuestData() {
	}

	public GuestData(String region, String[] categories, String lastName, int limit) {
		this.region = region;
		this.categories = categories;
		this.lastName = lastName;
		this.limit = limit;
	}

	public GuestData(String region, String[] categories) {
		this.region = region;
		this.categories = categories;
	}

	public GuestData(String name) {
		this.name = name;
	}

	public GuestData(String name, String creatorUsername) {
		this.name = name;
		this.creatorUsername = creatorUsername;
	}

}
