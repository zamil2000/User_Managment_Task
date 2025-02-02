package com.practice.user_management.mapper;

import org.springframework.stereotype.Component;

import com.practice.user_management.dto.UserRequestDto;
import com.practice.user_management.entity.User;

@Component
public class UserMapper {
	
	public User mapToEntity(UserRequestDto dto) {
		User user= new User();
		user.setName(dto.getName());
		user.setEmail(dto.getEmail());
		user.setGender(dto.getGender());
		user.setPassword(dto.getPassword());
		return user;
	}

}
