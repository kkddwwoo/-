package com.project.picasso.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import java.util.List;
import com.project.picasso.model.posts.Post;

public interface PostRepository {
	// 게시글 저장
	void savePost(Post post);

	// 글 전체 조회
	List<Post> findAllPosts(RowBounds rowBounds);

	// 아이디로 글 상세 조회
	Post findPostById(Long postId);

	// 조회수 수정
	void updatePost(Post post);

	// 글 삭제
	void removePost(Long postId);

	// 글 수정
	void editPost(Post post);

	// 마이페이지 글 조회
	List<Post> selectPostById(Long userId);
	
	// 마이페이지 글 조회
	List<Post> selectPostListById(Long userId, RowBounds rowBounds);

	// 전체 게시글 갯수
	int getTotal();

	// 내용으로 찾기
	List<Post> findByContentContaining(String keyword);

	// hash로 찾기
	List<Post> findByHashContaining(String keyword);

	// 내용으로 찾기
	List<Post> findByContentContaining(@Param("keyword") String keyword, @Param("userId") Long userId,
			RowBounds rowBounds);

	// hash로 찾기
	List<Post> findByHashContaining(@Param("keyword") String keyword, @Param("userId") Long userId,
			RowBounds rowBounds);
	
	// 페이징 용 글 찾기
	List<Post> pagePostId(Long userId, RowBounds rowBounds);

	// 전체 게시글 갯수
	int getUserTotal(Long userId);
	
	// 전체 게시글 갯수
	int findUserContentTotalSearch(@Param("keyword") String keyword, @Param("userId") Long userId);

	int findUserHashTotalSearch(@Param("keyword") String keyword, @Param("userId") Long userId);

}
