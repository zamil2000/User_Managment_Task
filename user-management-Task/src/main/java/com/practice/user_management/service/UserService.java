package com.practice.user_management.service;

import java.util.List;

import com.practice.user_management.entity.User;

import jakarta.validation.Valid;

public interface UserService {
	 String registerUser(@Valid User user);
	 String authenticateUser(String email, String password);
	 List<User> getAllUsers();
	String deleteUser(String email);
	
}
