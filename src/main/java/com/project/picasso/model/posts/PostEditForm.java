package com.project.picasso.model.posts;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import com.project.picasso.model.users.User;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostEditForm {
	private Long id;
	@NotBlank
	private String title;
	@NotBlank
	private String content;
	private String fileName;
	private User user;
	private int views;
	private LocalDateTime createTime;
}
