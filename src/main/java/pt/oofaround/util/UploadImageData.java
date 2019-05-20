package pt.oofaround.util;

public class UploadImageData {

	public String username;
	public byte[] image;
	public String photoName;

	public UploadImageData() {
	}

	public UploadImageData(String username, byte[] image) {
		this.username = username;
		this.image = image;
	}

	public UploadImageData(String username, byte[] image, String photoName) {
		this.username = username;
		this.image = image;
		this.photoName = photoName;
	}

}
