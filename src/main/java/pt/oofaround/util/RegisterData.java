package pt.oofaround.util;

public class RegisterData {

	public String username;
	public String password;
	public String email;
	public String country;
	public String cellphone;
	public String role;
	public boolean privacy;

	public RegisterData() {
	}

	public RegisterData(String username, String password, String email, String country, String cellphone,
			String privacy) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.country = country;
		this.cellphone = cellphone;
		if (privacy.equals("true")) {
			this.privacy = true;
		} else {
			this.privacy = false;
		}
	}

}
