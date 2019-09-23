package store.configuration.service.impl;

import java.util.Calendar;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import store.configuration.model.User;
import store.configuration.model.UserVerificationToken;
import store.configuration.model.UserVerificationTokenStatus;
import store.configuration.repository.UserRepository;
import store.configuration.repository.UserVerificationTokenRepository;
import store.configuration.service.UserVerificationTokenService;

@Service
public class UserVerificationTokenServiceImpl implements UserVerificationTokenService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	UserVerificationTokenRepository userVerificationTokenRepository;

	@Override
	@Transactional(readOnly = true)
	public Optional<UserVerificationToken> findByToken(String token) {
		return userVerificationTokenRepository.findByToken(token);
	}

	@Override
	public void createVerificationTokenForUser(User user, String token) {
		UserVerificationToken userVerificationToken = new UserVerificationToken(token, user);
		userVerificationTokenRepository.save(userVerificationToken);
	}

	@Override
	public UserVerificationTokenStatus validateVerificationToken(String token) {
		Optional<UserVerificationToken> verificationToken = userVerificationTokenRepository.findByToken(token);
		if (!verificationToken.isPresent()) {
			return UserVerificationTokenStatus.INVALID;
		}
		User user = verificationToken.get().getUser();
		Calendar cal = Calendar.getInstance();
		if ((verificationToken.get().getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			userVerificationTokenRepository.delete(verificationToken.get());
			return UserVerificationTokenStatus.EXPIRED;
		}
		user.setEnabled(true);
		user.setToken(null);
		userVerificationTokenRepository.delete(verificationToken.get());
		userRepository.saveAndFlush(user);
		return UserVerificationTokenStatus.VERIFIED;
	}

}
