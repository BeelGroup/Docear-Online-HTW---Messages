package controllers;

import models.backend.User;
import org.apache.commons.lang.Validate;
import play.Logger;
import play.cache.Cache;

import javax.annotation.Nullable;

public class Session {

    private static final String CACHE_KEY = "user-obj.";

	public static void createSession(String username, String accessToken) {
        Validate.notEmpty(username);
        Validate.notEmpty(accessToken);
        User user = new User(username, accessToken);
        Cache.set(CACHE_KEY + username, user);
        User.upsert(user);
	}
	
	public static User getUser(@Nullable String username) {
        User user = (User) Cache.get(CACHE_KEY + username);
        if (user == null && username != null) {
            Logger.debug("user '" + username + "' not in Cache, obtaining from database");
            user = User.findByName(username);
        }
        return user;
    }

}
