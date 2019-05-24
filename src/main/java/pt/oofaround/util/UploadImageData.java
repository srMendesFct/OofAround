package pt.oofaround.util;

public class UploadImageData {

	public String name;
	public byte[] image;
	public String usernameR;
	public String tokenId;
	public String role;

	public UploadImageData() {
	}

	public UploadImageData(String name, byte[] image, String usernameR, String tokenId, String role) {
		this.name = name;
		this.image = image;
		this.usernameR = usernameR;
		this.tokenId = tokenId;
		this.role = role;
	}

}
