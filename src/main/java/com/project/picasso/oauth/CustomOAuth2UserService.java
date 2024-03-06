package com.project.picasso.oauth;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.project.picasso.model.users.GenderType;
import com.project.picasso.model.users.User;
import com.project.picasso.repository.UserRepository;
import com.project.picasso.service.UserService;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserService userService;
	private final UserRepository userRepository;
	
	public CustomOAuth2UserService(UserRepository userRepository, UserService userService) {
		this.userRepository = userRepository;
		this.userService = userService;
		
	}
	
	
	@Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
		
		OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
	
		log.info("~~~값은? {}", oAuth2User);
		
		
		log.info("오스투유저리퀘스트 값은? {}", oAuth2UserRequest);
		log.info("~~~값은? {}", oAuth2UserRequest.getClientRegistration());
        
		 Map<String, Object> attribute = oAuth2User.getAttributes();
		
		//어트리뷰트 받아오기
		log.info("어트리뷰트이름: {}", attribute.get("name"));
		log.info("어트리뷰트이메일: {}", attribute.get("email"));
		log.info("어트리뷰트: {}", attribute);
		

		User user = new User();
		
		user.setEmail((String)attribute.get("email"));
		user.setUsername((String)attribute.get("email"));
		user.setName((String)attribute.get("name"));

		
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("1234");
		
		
		
		user.setPassword(encodedPassword);
		user.setGender(GenderType.MALE);
		user.setBirthDate(LocalDate.of(2000, 1, 1));
		user.setPhone("00000000000");
		user.setProfileName(null);
		
		 User findUser = userService.findUserByUsername(user.getUsername());
		
		 if (findUser != null) {
				
			 user.setId(findUser.getId());
				
				}
		
		
		
		ValidatedUser validatedUser = new ValidatedUser(user, attribute);
		
		log.info("인증유저: {}", validatedUser);
		
		
		registerIfNewUser(validatedUser);
		
		
		
		return validatedUser;
    }
	

	 
	 
	 private void registerIfNewUser(ValidatedUser validatedUser) {
	       
		 User findUser = userService.findUserByUsername(validatedUser.getUser().getUsername());
		
		if (findUser == null) {
		
			userService.registerUser(validatedUser.getUser());
			
			}

		 
	    }
	 
	 
}
