package com.project.picasso.model.posts;

import java.time.LocalDateTime;

import com.project.picasso.model.users.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Post {
	private Long id; // 게시글 아이
	private String content; // 내용
	private User user; // 작성자
	private int views; // 조회수
	private String fileName; // 첨부파일 파일명
	private LocalDateTime createTime; // 작성일자
	private String Hash;
	private String Hash2;
	private String Hash3;
}