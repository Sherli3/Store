package store.configuration.service;

import java.util.Optional;

import store.configuration.model.User;
import store.configuration.model.UserVerificationToken;
import store.configuration.model.UserVerificationTokenStatus;

public interface UserVerificationTokenService {
	Optional<UserVerificationToken> findByToken(String token);

	void createVerificationTokenForUser(User user, String token);

	UserVerificationTokenStatus validateVerificationToken(String token);

}
