package store.configuration.service.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import store.configuration.model.PasswordResetToken;
import store.configuration.model.Role;
import store.configuration.model.User;
import store.configuration.repository.PasswordResetTokenRepository;
import store.configuration.repository.RoleRepository;
import store.configuration.repository.UserRepository;

@Service
@Transactional
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	PasswordResetTokenRepository passwordTokenRepository;

	public User saveUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		Role userRole = roleRepository.findByRole("USER");
		if (userRole == null) {
			userRole = roleRepository.save(new Role("USER"));
		}
		user.setUserRoles(new HashSet<Role>(Arrays.asList(userRole)));
		user.setEnabled(false);
		return userRepository.save(user);
	}

	public void updateUser(User item) throws NoSuchElementException {
		User user = userRepository.getById(item.getId()).get();
		if (item.getEmail() != null && item.getEmail().length() > 0) {
			user.setEmail(item.getEmail());
		}
		if (item.getFirstName() != null && item.getFirstName().length() > 0) {
			user.setFirstName(item.getFirstName());
		}
		if (item.getLastName() != null && item.getLastName().length() > 0) {
			user.setLastName(item.getLastName());
		}
		if (item.getPassword() != null && item.getPassword().length() > 0) {
			user.setPassword(bCryptPasswordEncoder.encode(item.getPassword()));
		}
		userRepository.saveAndFlush(item);
	}

	public void createPasswordResetTokenForUser(final User user, final String token) {
		final PasswordResetToken myToken = new PasswordResetToken(token, user);
		passwordTokenRepository.save(myToken);
	}

	public void changeUserPassword(User user, final String password) {
		user.setPassword(bCryptPasswordEncoder.encode(password));
		userRepository.save(user);
	}

	public PasswordResetToken getPasswordResetToken(final String token) {
		return passwordTokenRepository.findByToken(token);
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

}
