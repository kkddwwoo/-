package com.project.picasso.repository;

import com.project.picasso.model.users.User;

public interface UserRepository {
	// 회원정보 등록
	void saveUser(User user);
	
	// username으로 회원정보 조회
	User findUserByUsername(String username);	
	
	void updateUser(User user);
	
	// id로 회원정보 조회
	User findUserById(Long userId);
}
