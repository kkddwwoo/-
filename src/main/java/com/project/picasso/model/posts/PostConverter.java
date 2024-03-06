package com.project.picasso.model.posts;

import java.time.LocalDateTime;

public class PostConverter {
	
	
	public static PostEditForm postToPostEditForm(Post post) {
		PostEditForm postEditForm = new PostEditForm();
		postEditForm.setId(post.getId());
		postEditForm.setContent(post.getContent());
		postEditForm.setViews(post.getViews());
		postEditForm.setCreateTime(post.getCreateTime());
		return postEditForm;
	}
	
	public static Post postEditFormToPost(PostEditForm postEditForm) {
		Post post = new Post();
		post.setId(postEditForm.getId());
		post.setContent(postEditForm.getContent());
		post.setFileName(postEditForm.getFileName());
		return post;
	}
}
