package backend;

public class User {
	private final String accesToken;
	private final String username;
	
	public User(final String username, final String accesToken) {
		this.accesToken = accesToken;
		this.username = username;
	}

	public String getAccesToken() {
		return accesToken;
	}

	public String getUsername() {
		return username;
	}
}
