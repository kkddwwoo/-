package com.project.picasso.model.users;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UserUpdateForm {
	private Long id;				// 일련번호
	private String username;		// 로그인 아이디
	private String password;		// 로그인 패스워드
	private String name;			// 이름
	private GenderType gender;		// 성별
	private LocalDate birthDate;	// 생년월일
	private String email;
}
