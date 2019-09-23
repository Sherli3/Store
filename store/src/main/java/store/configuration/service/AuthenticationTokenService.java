package store.configuration.service;

import org.springframework.security.core.userdetails.User;

public interface AuthenticationTokenService {
	String generateToken(String email);

	User getUserFromToken(String token);

}
