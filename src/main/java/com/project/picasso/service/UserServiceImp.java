package com.project.picasso.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.project.picasso.model.posts.Post;
import com.project.picasso.model.users.User;
import com.project.picasso.repository.PostRepository;
import com.project.picasso.repository.UserRepository;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImp implements UserService {

	private final UserRepository userRepository;
	private final PostRepository postRepository;
	
	@Override
	public User registerUser(User user) {
		userRepository.saveUser(user);
		return user;
	}

	@Override
	public User findUserByUsername(String username) {
		return userRepository.findUserByUsername(username);
	}

	@Override
	public User updateUser(User user) {
		userRepository.updateUser(user);
		return user;
	}
	
	//마이페이지에 작성글 불러오는 매서드
	@Override
	public List<Post> getIdPost(Long userId) {
		return postRepository.selectPostById(userId);
	}

	//마이페이지에 작성글 불러오는 매서드
	@Override
	public List<Post> getIdListPost(Long userId,int startRecord, int pagePerCount) {
		RowBounds rowBounds = new RowBounds(startRecord, pagePerCount);
		return postRepository.selectPostListById(userId,rowBounds);
	}
		
	@Override
	public User findUserById(Long userId) {
		return userRepository.findUserById(userId);
	}
	
	//페이징 갯수를 불러오는 메서드
	@Override
	public List<Post> getPostPage(Long userId, int startRecord, int pagePerCount) {
		RowBounds rowBounds = new RowBounds(startRecord, pagePerCount);
		return postRepository.pagePostId(userId, rowBounds);
	}

}
