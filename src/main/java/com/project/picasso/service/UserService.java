package com.project.picasso.service;

import java.util.List;

import com.project.picasso.model.posts.Post;
import com.project.picasso.model.users.User;
import com.project.picasso.model.users.UserRegisterForm;

public interface UserService {
	// 회원가입
	User registerUser(User user);

	// 로그인 (username으로 회원정보 조회)
	User findUserByUsername(String username);

	// 회원정보 수정
	User updateUser(User user);

	// 아이디에 해당하는 글 리스트로 조회
	List<Post> getIdPost(Long userId);

	// 아이디에 해당하는 글 리스트로 조회
	List<Post> getIdListPost(Long userId,int startRecord, int pagePerCount);
	
	// 회원정보 찾기
	User findUserById(Long userId);

	// 페이징 전용
	List<Post> getPostPage(Long userId, int startRecord, int pagePerCount);
}
