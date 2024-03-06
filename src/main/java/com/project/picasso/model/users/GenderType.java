package com.project.picasso.model.users;

import lombok.Getter;

@Getter
public enum GenderType {
	MALE("男性"),
	FEMALE("女性");
	
	private final String description;
	
	GenderType(String description) {
		this.description = description;
	}
}

