package com.project.picasso.oauth;

import java.util.Map;

import com.project.picasso.model.users.User;

import lombok.Getter;


@Getter
public class UserAdapter extends ValidatedUser {

	private User user;
	private Map<String, Object>attribute;
	
	public UserAdapter(User user) {
		super(user);
		this.user = user;
	}

	public UserAdapter(User user, Map<String, Object> attribute) {
		super(user, attribute);
		this.user = user;
		this.attribute = attribute;
	}
	
	
}
