
package com.practice.user_management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import com.practice.user_management.entity.User;
import com.practice.user_management.exception.EmailAlreadyExistsException;
import com.practice.user_management.repository.UserRepository;
import com.practice.user_management.service.UserImpService;

class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private UserImpService userService;

	private User user;
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		user = new User();
		user.setEmail("test@example.com");
		user.setPassword("password123");
		user.setName("Test User");
		user.setGender("Male");
		user.setIpAddress("192.168.1.1");
		user.setCountry("USA");
	}

	@Test
	void testRegisterUser_Success() {
		when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
		when(restTemplate.getForObject(anyString(), eq(UserImpService.IpResponse.class)))
				.thenReturn(new UserImpService.IpResponse());
		when(restTemplate.getForObject(anyString(), eq(UserImpService.IpCountryApiResponse.class)))
				.thenReturn(new UserImpService.IpCountryApiResponse());

		String result = userService.registerUser(user);
		assertEquals("User registered successfully", result);
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	void testRegisterUser_EmailAlreadyExists() {
		when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);
		assertThrows(EmailAlreadyExistsException.class, () -> userService.registerUser(user));
	}

	@Test
	void testAuthenticateUser_Success() {
		user.setPassword(passwordEncoder.encode("password123"));
		when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

		String result = userService.authenticateUser("test@example.com", "password123");
		assertEquals("Authentication successful", result);
	}

	@Test
	void testAuthenticateUser_WrongPassword() {
		user.setPassword(passwordEncoder.encode("password123"));
		when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

		String result = userService.authenticateUser("test@example.com", "wrongpassword");
		assertEquals("Invalid credentials", result);
	}

	@Test
	void testGetAllUsers() {
		when(userRepository.findAll()).thenReturn(List.of(user));
		List<User> users = userService.getAllUsers();
		assertEquals(1, users.size());
	}

	@Test
	void testDeleteUser_Success() {
		when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
		String result = userService.deleteUser("test@example.com");
		assertEquals("User deleted successfully", result);
	}

	@Test
	void testDeleteUser_NotFound() {
		when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());
		String result = userService.deleteUser("nonexistent@example.com");
		assertEquals("User not found", result);
	}
}
