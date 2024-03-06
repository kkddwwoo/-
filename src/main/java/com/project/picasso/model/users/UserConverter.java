package com.project.picasso.model.users;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.project.picasso.model.users.User;
import com.project.picasso.model.users.UserRegisterForm;

public class UserConverter {
	public static User userRegisterFormToUser(UserRegisterForm userRegisterForm) {
		User user = new User();
		user.setUsername(userRegisterForm.getUsername());
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(userRegisterForm.getPassword());
		
		user.setPassword(encodedPassword);	
		user.setName(userRegisterForm.getName());
		user.setGender(userRegisterForm.getGender());
		user.setBirthDate(userRegisterForm.getBirthDate());
		user.setEmail(userRegisterForm.getEmail());
		user.setPhone(userRegisterForm.getPhone());
		
		return user;
	}

	public static UserUpdateForm userToUserUpdateForm(User user) {
		UserUpdateForm userUpdateForm = new UserUpdateForm();
		userUpdateForm.setId(user.getId());
		userUpdateForm.setUsername(user.getUsername());
		userUpdateForm.setPassword(user.getPassword());
		userUpdateForm.setName(user.getName());
		userUpdateForm.setGender(user.getGender());
		userUpdateForm.setBirthDate(user.getBirthDate());
		userUpdateForm.setEmail(user.getEmail());
		
		return userUpdateForm;
	}
	
	public static User userPlusForm(UserPlusForm userPlusForm) {
		User user = new User();
		user.setId(userPlusForm.getId());
		user.setName(userPlusForm.getName());
		user.setProfileName(userPlusForm.getProfileName());
		return user;
	}
}
