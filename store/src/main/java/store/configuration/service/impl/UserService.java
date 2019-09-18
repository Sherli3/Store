package store.configuration.service.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import store.configuration.model.Role;
import store.configuration.model.User;
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
	private MailSender mailSender;

	public User saveUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		Role userRole = roleRepository.findByRole("USER");
		if (userRole == null) {
			userRole = roleRepository.save(new Role("USER"));
		}
		user.setUserRoles(new HashSet<Role>(Arrays.asList(userRole)));
		user.setActiveCode(UUID.randomUUID().toString());
		if (!StringUtils.isEmpty(user.getEmail())) {
			String message = String.format(
					"Hello, %s! \n"
							+ "Welcome to Store. Please, congiger link: http://localhost:8080/store/user/activate/%s",
					user.getFirstName(), user.getActiveCode());

			mailSender.send(user.getEmail(), "Activation code", message);
		}
		return userRepository.save(user);

	}

	public boolean activateUser(String code) {
		User user = userRepository.findByActiveCode(code);

		if (user == null) {
			return false;
		}
		user.setActiveCode(null);
		userRepository.save(user);
		return true;
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

}
