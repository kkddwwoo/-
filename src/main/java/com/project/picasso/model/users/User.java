package com.project.picasso.model.users;

import java.time.LocalDate;

import com.project.picasso.model.users.GenderType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {

	private Long id;				// 일련번호
	private String username;		// 로그인 아이디
	private String password;		// 로그인 패스워드
	private String name;			// 이름
	private GenderType gender;		// 성별
	private LocalDate birthDate;	// 생년월일
	private String email;			// 이메일 주소
	private String phone;			//전화번호
	private String profileName;		//프로필 사진
	
	public User(String profileName) {
		this.profileName = profileName;
	}
	
}
