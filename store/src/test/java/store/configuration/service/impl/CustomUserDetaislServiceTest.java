package store.configuration.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import store.configuration.model.Role;
import store.configuration.model.User;
import store.configuration.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class CustomUserDetaislServiceTest {
	@Mock
	private UserRepository userRepository;
	@InjectMocks
	private CustomUserDetaislService userDetailsService;
	private User user;
	private Role role;

	@Before
	public void setUp() throws Exception {
		user = new User();
		user.setId((long) 1);
		user.setEmail("test@email.com");
		user.setFirstName("test first");
		user.setLastName("test last");
		user.setPassword("test password");
		user.setEnabled(true);
		role = new Role();
		role.setId((int) 1);
		role.setRole("TEST");
		Set<Role> roleSet = new HashSet<>();
		roleSet.add(role);
		user.setUserRoles(roleSet);
	}

	@Test
	public void testLoadUserByUsername() {
		when(userRepository.findByEmail("test@email.com")).thenReturn((user));
		UserDetails userDetails = userDetailsService.loadUserByUsername("test@email.com");
		assertNotNull(userDetails);
		assertEquals(userDetails.getPassword(), user.getPassword());
		assertEquals(userDetails.getUsername(), user.getEmail());
		assertNotNull(userDetails.getAuthorities());
		assertNotEquals(userDetails.getAuthorities(), 0);
		assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority(role.getRole())));

	}

}
