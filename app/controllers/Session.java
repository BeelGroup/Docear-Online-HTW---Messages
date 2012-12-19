package controllers;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import backend.User;

public class Session {

	private final static Map<String, User> SESSION_MAP;
	private final static SecureRandom RANDOM;

	static {
		SESSION_MAP = new HashMap<String, User>();
		RANDOM = new SecureRandom();
	}
	
	

	public static String createSession(String username, String accessToken) {
		String sessionId;
		
		do{
		StringBuilder builder = new StringBuilder();
		builder.append(username.hashCode());
		builder.append(new BigInteger(130, RANDOM).toString(32));
		
		sessionId = builder.toString();
		} while (SESSION_MAP.containsKey(sessionId));
		
		User user = new User(username, accessToken);
		
		SESSION_MAP.put(sessionId, user);

		return sessionId;
	}

}
