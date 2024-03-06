package com.project.picasso.oauth;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.project.picasso.model.users.User;

import lombok.Data;


@Data
public class ValidatedUser implements OAuth2User, UserDetails {

	
	private final User user;
	private Map<String, Object> attribute;
	private Collection<? extends GrantedAuthority> authorities;
	
	
	public ValidatedUser(User user, Map<String, Object> attribute) {
		this.user = user;
		this.attribute = attribute;
		this.authorities = authorities;
	}
	
	public ValidatedUser(User user) {
		this.user = user;
	}
	
	@Override
	public Map<String, Object> getAttributes() {
		return attribute;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getName() {
		return user.getName();
	}
	
	@Override
	public String getPassword() {
		 if (user != null) {
		        return user.getPassword();
		    } else {
		        return null;
		    }
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

/*UserDetails 구현
* 계정 만료 여부
* true: 만료 안됨
* false: 만료됨
*/
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

/*UserDetails 구현
* 계정 잠김 여부
* true: 잠기지 않음
* false: 잠금
*/
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

/*UserDetails 구현
 * 계정 비밀번호 만료 여부
 * true: 만료 안됨
 * false: 만료됨
 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

/*
 * UserDetails 구현
 * 계정 활성화 여부
 * true: 활성화 됨
 * false: 활성화 안 됨
 */
	@Override
	public boolean isEnabled() {
		return true;
	}

}
