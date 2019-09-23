package store.configuration.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import store.configuration.model.UserVerificationToken;

public interface UserVerificationTokenRepository extends JpaRepository<UserVerificationToken, Long> {

	Optional<UserVerificationToken> findByToken(String token);
	
}
