package pt.oofaround.util;

public class AuthenticationTool {

	public AuthenticationTool() {
	}
	
	public static boolean authenticate(String tokenID, String username, String role, String action, long expirationDate) {
		
		if(expirationDate < System.currentTimeMillis())
			return false;
		
		AuthToken at = new AuthToken(username, role, expirationDate);
		
		if(!at.tokenID.equalsIgnoreCase(tokenID))
			return false;
		
		//TODO verificar se o user como dado role pode realizar esta operacao
		
		return true;
	}

}
