package services.backend.user;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Profile("backendMock")
@Component
public class MockUserService implements UserService{

	@Override
	public String authenticate(String username, String password) {
		throw new NotImplementedException();
	}

}
