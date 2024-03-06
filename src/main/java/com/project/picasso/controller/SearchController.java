package com.project.picasso.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.project.picasso.model.posts.Post;
import com.project.picasso.model.users.User;
import com.project.picasso.oauth.ValidatedUser;
import com.project.picasso.service.PostService;
import com.project.picasso.service.UserService;
import com.project.picasso.util.PageNavigator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class SearchController {

	@Autowired
	private UserService userService;
	private int countPerPage = 6;
	private int pagePerGroup = 5;
	
	@Autowired
	private PostService postService;
	

	@GetMapping("/search")
	public String search(Authentication authentication,
			@RequestParam("searchType") String searchType, @RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(name = "page", defaultValue = "1") int page,Model model) {
		ValidatedUser loginUser = (ValidatedUser) authentication.getPrincipal();
		
	    User user = userService.findUserById(loginUser.getUser().getId());
		if (keyword == null || keyword.isEmpty()) {
		    // 키워드가 없는 경우 처리
		    model.addAttribute("message", "검색어를 입력해주세요.");
		    return "redirect:/list"; // /list로 리다이렉트
		}
	    int total = postService.getUserTotalSearch(user.getId(),searchType,keyword);
	    log.info("total:"+total);
		PageNavigator navi = new PageNavigator(countPerPage, pagePerGroup, page, total);
		log.info("navi:"+navi);
	    List<Post> posts = postService.search(searchType, keyword,user.getId(),navi.getStartRecord(), navi.getCountPerPage());
	    if (posts == null || posts.isEmpty()) {
	        // 검색 결과가 없는 경우 처리
	        model.addAttribute("message", "검색 결과가 없습니다.");
	        posts = new ArrayList<>(); // 빈 리스트 생성
	        model.addAttribute("userposts", posts);
		    model.addAttribute("navi",navi);
	        
	    }else {
	    	
	    	model.addAttribute("userposts", posts);
		    model.addAttribute("navi",navi);
	    }
	    
	    
	    return "listSearch";
	}

}
