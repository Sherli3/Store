package store.configuration.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import store.configuration.model.User;
import store.configuration.model.UserVerificationToken;
import store.configuration.model.UserVerificationTokenStatus;
import store.configuration.repository.UserRepository;
import store.configuration.repository.UserVerificationTokenRepository;
import store.configuration.service.impl.UserVerificationTokenServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class UserVerificationTokenServiceTest {
	@Mock
	private UserRepository userRepository;
	@Mock
	UserVerificationTokenRepository tokenRepository;
	@InjectMocks
	private UserVerificationTokenServiceImpl tokenService;

	@Test
	public void testValidateInvalidVerificationToken() {
		when(tokenRepository.findByToken("")).thenReturn(Optional.empty());
		UserVerificationTokenStatus status = tokenService.validateVerificationToken("");
		assertNotNull(status);
		assertEquals(status, UserVerificationTokenStatus.INVALID);
	}

	@Test
	public void testValidateExpiredVerificationToken() {
		UserVerificationToken token = new UserVerificationToken("expired");
		Calendar.getInstance().set(2000, 11, 11);
		token.setExpiryDate(Calendar.getInstance().getTime());
		when(tokenRepository.findByToken(token.getToken())).thenReturn(Optional.of(token));
		UserVerificationTokenStatus status = tokenService.validateVerificationToken(token.getToken());
		assertNotNull(status);
		assertEquals(status, UserVerificationTokenStatus.EXPIRED);
	}

	@Test
	public void testValidateVerifiedVerificationToken() {
		UserVerificationToken token = new UserVerificationToken("valid");
		token.setUser(new User());
		when(tokenRepository.findByToken(token.getToken())).thenReturn(Optional.of(token));
		UserVerificationTokenStatus status = tokenService.validateVerificationToken(token.getToken());
		assertNotNull(status);
		assertEquals(status, UserVerificationTokenStatus.VERIFIED);
	}

}
