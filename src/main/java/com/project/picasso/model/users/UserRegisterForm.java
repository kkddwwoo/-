package com.project.picasso.model.users;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.project.picasso.model.users.GenderType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRegisterForm {
	// 6자리 이상 20자리 이하
	@Size(min = 6, max = 20, message = "IDは6-20文字で入力してください。")
	private String username;		// 로그인 아이디
	@Size(min = 6, max = 20, message = "パスワードは6-20文字で入力してください。")
	private String password;		// 로그인 패스워드
	@NotBlank
	private String name;			// 이름
	@NotNull(message = "必ず選択してください。")
	private GenderType gender;		// 성별
	@Past
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthDate;	// 생년월일
	private String email;			// 이메일 주소
	private String phone;			// 폰 번호
}
