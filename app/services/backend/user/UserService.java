package services.backend.user;

public interface UserService {

	/**
	 * authenticates user credentials
	 * @param username
	 * @param password
	 * @return user access token or null on failure
	 */
	String authenticate(String username, String password);
	
}
