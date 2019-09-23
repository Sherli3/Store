package store.configuration.service;

public interface PasswordResetTokenService {

	String validatePasswordResetToken(long id, String token);
}
