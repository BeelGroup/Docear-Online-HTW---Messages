package services.backend.user;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import play.libs.WS;
import play.libs.WS.Response;



@Profile("backendProd")
@Component
public class ServerUserService implements UserService {

	@Override
	public String authenticate(String username, String password) {
		Response response = WS.url("https://api.docear.org/authenticate/"+username)
				.post("password="+password).get();
		
		if(response.getStatus() == 200) { //succesful authentication
			String accessToken = response.getHeader("accessToken");
			return accessToken;
		} else {
			return null;
		}
	}

}
