package com.project.picasso.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.picasso.oauth.ValidatedUser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {

		@GetMapping("/")
		public String home(@AuthenticationPrincipal ValidatedUser validatedUser) {
		//	log.info("유저유저유저유저: {}", validatedUser.getUser());
			log.info("Home 요청");
			return "index";
		}
		
	
}