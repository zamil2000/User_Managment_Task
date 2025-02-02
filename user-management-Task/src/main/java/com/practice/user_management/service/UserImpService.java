package com.practice.user_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.practice.user_management.entity.User;
import com.practice.user_management.exception.EmailAlreadyExistsException;
import com.practice.user_management.repository.UserRepository;

@Service
public class UserImpService implements UserService {

	@Autowired
	private UserRepository userRepository;

	private String ipUrl="https://api64.ipify.org?format=json";

	private String conUrl="http://ip-api.com/json/";

	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	private final RestTemplate restTemplate = new RestTemplate();

	public static class IpResponse {
		private String ip;

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}
	}

	public static class IpCountryApiResponse {
		private String country;

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}
	}

	public String registerUser(User user) {

		if (userRepository.existsByEmail(user.getEmail())) {
			throw new EmailAlreadyExistsException("Email already registered");
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		IpResponse ip = restTemplate.getForObject(ipUrl, IpResponse.class);

		user.setIpAddress(ip.getIp());

		IpCountryApiResponse country = restTemplate.getForObject(conUrl, IpCountryApiResponse.class);
		user.setCountry(country.getCountry());

		userRepository.save(user);
		return "User registered successfully";
	}

	public String authenticateUser(String email, String password) {
		Optional<User> userOpt = userRepository.findByEmail(email);
		if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
			return "Authentication successful";
		}
		return "Invalid credentials";
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public String deleteUser(String email) {
		Optional<User> userOpt = userRepository.findByEmail(email);
		if (userOpt.isPresent()) {
			userRepository.delete(userOpt.get());
			return "User deleted successfully";
		}
		return "User not found";
	}
}
