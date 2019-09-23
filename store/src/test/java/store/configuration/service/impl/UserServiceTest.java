package store.configuration.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import store.configuration.model.Role;
import store.configuration.model.User;
import store.configuration.repository.RoleRepository;
import store.configuration.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
	@Mock
	private UserRepository userRepository;
	@Mock
	private RoleRepository roleRepository;
	@InjectMocks
	private UserService userService;
	@Mock
	private BCryptPasswordEncoder encoder;
	private User user;
	private Role role;

	@Before
	public void setUp() throws Exception {
		user = new User();
		user.setFirstName("first");
		user.setLastName("last");
		user.setEmail("user@test.com");
		role = new Role();
		role.setId((int) 1);
		role.setRole("TEST");
	}

	@After
	public void tearDown() throws Exception {
		user = null;
		role = null;
	}

	@Test
	public void saveUser() {
		when(roleRepository.findByRole("USER")).thenReturn(role);
		when(encoder.encode(user.getPassword())).thenReturn(user.getPassword());
		when(userRepository.save(user)).thenReturn(user);
		User resultUser = userService.saveUser(user);
		assertFalse(resultUser.getEnabled());
		assertTrue(resultUser.getUserRoles().contains(role));
		assertEquals(resultUser.getPassword(), user.getPassword());

	}

}
