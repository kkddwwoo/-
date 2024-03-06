package com.project.picasso.model.users;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserPlusForm {
	private Long id;
	@NotBlank
	private String name;			// 이름
	private String profileName;		// 프로필 파일
}
