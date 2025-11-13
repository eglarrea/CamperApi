package hemen.go.dto.response;

import java.util.Optional;

public class JwtResponse {
	 	private String token;
	 	private UserDtoResponse user;
	 	
	    public JwtResponse() {}

	    public JwtResponse(String token, UserDtoResponse user) {
	        this.token = token;
	        this.user = user;
	    }

	    public UserDtoResponse getUser() {
			return user;
		}

		public void setUser(UserDtoResponse user) {
			this.user = user;
		}

		public String getToken() {
	        return token;
	    }

	    public void setToken(String token) {
	        this.token = token;
	    }
}
