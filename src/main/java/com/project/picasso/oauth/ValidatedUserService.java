package com.project.picasso.oauth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.picasso.model.users.User;
import com.project.picasso.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ValidatedUserService implements UserDetailsService {

	
	private final UserRepository userRepository;
	private final HttpSession session;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User byUsername = userRepository.findUserByUsername(username);
        if(byUsername != null){
            return new ValidatedUser(byUsername);
        }
        log.info("바이유저네임 뭐지? {}", byUsername);
        return new UserAdapter(byUsername);
	}
	
}



