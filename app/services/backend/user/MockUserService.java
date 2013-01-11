package services.backend.user;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;


@Profile("backendMock")
@Component
public class MockUserService implements UserService{

	@Override
	public String authenticate(String username, String password) {
        final boolean usernameCorrect = Arrays.asList("Jöran", "Julius", "Michael", "Florian", "Alex", "Paul", "Marcel", "Dimitri", "Volker").contains(username);
        final boolean authenticated = usernameCorrect && "secret".equals(password);
        return authenticated ? username + "-token-" + UUID.randomUUID().toString() :null;
    }

}
