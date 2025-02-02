package com.practice.user_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.practice.user_management.dto.UserRequestDto;
import com.practice.user_management.entity.User;
import com.practice.user_management.mapper.UserMapper;
import com.practice.user_management.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	UserMapper mapper;

	@GetMapping("/health")
	public String healthCheck() {
		return "Application is running healthy!";
	}

	@Operation(summary = "Register a new user", description = "Creates a new user")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "User created successfully"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") })
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@Valid @RequestBody UserRequestDto userDto) {

		User user = new User();
		user = mapper.mapToEntity(userDto);

		String response = userService.registerUser(user);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Operation(summary = "Authenticate the user", description = "check the user Authenticate")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "User Authenticate successfully"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") })
	@GetMapping("/authenticate")
	public ResponseEntity<String> authenticateUser(@RequestParam String email, @RequestParam String password) {
		try {
			String response = userService.authenticateUser(email, password);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error authenticating user: " + e.getMessage());
		}
	}

	@Operation(summary = "Get all users", description = "Fetch a list of all registered users")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "successfully  successfully"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") })
	@GetMapping("/all")
	public ResponseEntity<List<User>> getAllUsers() {
		try {
			return ResponseEntity.ok(userService.getAllUsers());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@Operation(summary = "Delete the user based on email", description = "Admin can delete the user by emailid")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "User  Deleted successfully"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") })
	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteUser(@RequestParam String email) {
		try {
			String response = userService.deleteUser(email);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error deleting user: " + e.getMessage());
		}
	}
}
