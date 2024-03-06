package com.project.picasso.oauth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class OAuth2LoginSecurityConfig {

	private final CustomOAuth2UserService customOAuth2Userservice;
	private final CustomLoginSucessHandler customLoginSucessHandler;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(authorize -> authorize
					.requestMatchers("/","/loginform","/images/**", "/register","/fonts/**").permitAll()
					.anyRequest().authenticated()
			)
			.formLogin(formLogin->formLogin
					.loginPage("/loginform")
					.loginProcessingUrl("/login")
					.usernameParameter("username")
                    .passwordParameter("password")
					.defaultSuccessUrl("/")
					.successHandler(customLoginSucessHandler)
					.permitAll()
			)
		     .headers(
	                    headersConfigurer ->
	                            headersConfigurer
	                                    .frameOptions(
	                                            HeadersConfigurer.FrameOptionsConfig::sameOrigin
	                                    )
	            )
	//		.httpBasic(Customizer.withDefaults())
			.oauth2Login((oauth2) -> oauth2
					.loginPage("/loginform")
                  //  .loginPage("/oauth2/authorization/google") // 권한 접근 실패 시 로그인 페이지로 이동
                   .defaultSuccessUrl("/") // 로그인 성공 시 이동할 페이지
                    .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2Userservice))
                   // .failureUrl("/oauth2/authorization/google") // 로그인 실패 시 이동 페이지     
                    .successHandler(customLoginSucessHandler)
            )
			 .logout((logout) -> logout.logoutUrl("/logout").logoutSuccessUrl("/"));
			
		return http.build();
	}
}