package com.project.picasso.service;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.picasso.model.posts.Post;
import com.project.picasso.model.users.User;
import com.project.picasso.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private final PostRepository postRepository;
	
	@Override
	public Post savePost(Post post) {
		post.setCreateTime(LocalDateTime.now());
		postRepository.savePost(post);
		return post;
	}

	@Override
	public List<Post> getAllPosts(int startRecord, int pagePerCount) {
		RowBounds rowBounds = new RowBounds(startRecord, pagePerCount);
        return postRepository.findAllPosts(rowBounds);
	}

	@Override
	public Post getPostById(Long postId) {
		Post post = postRepository.findPostById(postId);
		if (post != null) {
			post.setViews(post.getViews() + 1);
			postRepository.updatePost(post);			
		}
		return post;
	}

	@Override
	public void removePost(Long postId, User loginUser) {
		Post post = postRepository.findPostById(postId);
		if (post != null && loginUser != null) {
			if (post.getUser().getId().equals(loginUser.getId())) {
				postRepository.removePost(postId);
			}
		}
	}

	@Override
	public int getTotal() {
		return postRepository.getTotal();
	}
	
	@Override
	public Post readPost(Long postId) {
		Post post = postRepository.findPostById(postId);
		postRepository.updatePost(post);
        return post;
	}
	
	@Override
	public Post updatePost(Post post) {
		Post findPost = postRepository.findPostById(post.getId());
		postRepository.updatePost(post);
		return post;
	}

    @Override
    public List<Post> search(String searchType, String keyword,Long userId,int startRecord, int pagePerCount) {
    	RowBounds rowBounds = new RowBounds(startRecord, pagePerCount);
        if ("content".equals(searchType)) {
            return postRepository.findByContentContaining(keyword,userId,rowBounds);
        } else if ("hash".equals(searchType)) {
            return postRepository.findByHashContaining(keyword,userId,rowBounds);
        }
        return null;
    }
    
    @Override
    public int getUserTotal(Long userId) {
    	return postRepository.getUserTotal(userId);
    }
    
    @Override
    public int getUserTotalSearch(Long userId,String searchType,String keyword) {
        if ("content".equals(searchType)) {
            return postRepository.findUserContentTotalSearch(keyword,userId);
        } else if ("hash".equals(searchType)) {
            return postRepository.findUserHashTotalSearch(keyword,userId);
        }
        return 0;
    }
}
