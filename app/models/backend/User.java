package models.backend;

import org.apache.commons.lang.Validate;
import play.Logger;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User extends Model {
    private static final Model.Finder<String,User> find = new Model.Finder<String,User>(String.class, User.class);
	private String accessToken;
    @Id
	private String username;
	
	public User(final String username, final String accessToken) {
		this.accessToken = accessToken;
		this.username = username;
	}

    public static User findByName(final String username) {
        return find.byId(username);
    }

    //TODO Michael refactor
    public static User upsert(final User user) {
        Validate.notNull(user);
        final User oldData = find.byId(user.getUsername());
        User resultingUser = user;
        final boolean isNewEntry = oldData == null || oldData.getUsername() == null;
        if (isNewEntry) {
            user.save();
        } else {
            oldData.setAccessToken(user.getAccessToken());
            oldData.save();
            resultingUser = oldData;
        }
        return resultingUser;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "accessToken='" + accessToken + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
