package com.project.picasso.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.project.picasso.cloud.CloudImageGenerator;
import com.project.picasso.cloud.WordCloudService;
import com.project.picasso.model.posts.Post;
import com.project.picasso.model.users.User;
import com.project.picasso.model.users.UserConverter;
import com.project.picasso.model.users.UserPlusForm;
import com.project.picasso.model.users.UserRegisterForm;
import com.project.picasso.oauth.ValidatedUser;
import com.project.picasso.service.PostService;
import com.project.picasso.service.UserService;
import com.project.picasso.util.FileService;
import com.project.picasso.util.PageNavigator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {

	private final UserService userService;
	private final PostService postService;
	private final FileService fileService;
	private int countPerPage = 3;
	private int pagePerGroup = 3;

	@Value("${file.upload.path}")
	private String uploadPath;
	private String path = "C:\\workspace\\upload\\";

	// 로그인폼 이동
	@GetMapping("loginform")
	public String loginForm() {
		return "loginform"; // 로그인 폼의 뷰 이름
	}

	// 회원 가입 폼 이동
	@GetMapping("register")
	public String register(Model model) {
		UserRegisterForm userRegisterForm = new UserRegisterForm();
		model.addAttribute("userRegisterForm", userRegisterForm);
		return "/register";
	}

	// 회원가입 처리
	@PostMapping("register")
	public String register(@Validated @ModelAttribute UserRegisterForm userRegisterForm, BindingResult bindingResult) {
		// 유효성 검증에 실패하면 실행
		if (bindingResult.hasErrors()) {
			return "/register";
		}

		// 아이디 중복 확인
		User findUser = userService.findUserByUsername(userRegisterForm.getUsername());
		if (findUser != null) {
			bindingResult.reject("duplicate username", "이미 가입된 아이디 입니다.");
			return "/register";
		}

		User user = UserConverter.userRegisterFormToUser(userRegisterForm);
		userService.registerUser(user);
		return "redirect:/";
	}

	// 로그아웃 처리
	@GetMapping("logout")
	public String logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		session.removeAttribute("loginUser");

		if (session != null) {
			session.invalidate();
		}

		return "redirect:/";
	}

	// 마이페이지 보기
	@GetMapping("mypage")
	public String myPageForm(Authentication authentication, 
							@RequestParam(name = "page", defaultValue = "1") int page,
							Model model) {
		ValidatedUser loginUser = (ValidatedUser) authentication.getPrincipal();
		if (loginUser != null) {
			User user = userService.findUserById(loginUser.getUser().getId());
			List<Post> userPosts = userService.getIdPost(loginUser.getUser().getId());
			
			int total = postService.getUserTotal(user.getId());
			PageNavigator navi = new PageNavigator(countPerPage, pagePerGroup, page, total);
			List<Post> naviUser = userService.getPostPage(loginUser.getUser().getId(), navi.getStartRecord(), navi.getCountPerPage());
			model.addAttribute("naviUser", naviUser);
			model.addAttribute("user", user);
			model.addAttribute("navi", navi);
		}
		return "mypage";
	}

	// 프로필 수정
	@GetMapping("plus/{userId}")
	public String plusMyPageForm(Authentication authentication, Model model) {
		ValidatedUser loginUser = (ValidatedUser) authentication.getPrincipal();
		if (loginUser != null) {
			User findUser = userService.findUserById(loginUser.getUser().getId());
			model.addAttribute("user", findUser);
		}

		return "plus";
	}

	// 프로필 수정
	@PostMapping("plus/{userId}")
	public String plusMyPage(Authentication authentication, @PathVariable(name = "userId") Long userId,
			@ModelAttribute UserPlusForm userPlusForm,
			@RequestParam(name = "file", required = false) MultipartFile file, Model model) {

		ValidatedUser loginUser = (ValidatedUser) authentication.getPrincipal();

		User findUser = userService.findUserById(loginUser.getUser().getId());

		// 파일명 생성
		String filenameWithExtension = fileService.generateUniqueFilename() + ".png";

		// 이미지를 서버에 저장하고 지정된 파일명을 반환받음
		String filePath = fileService.saveProfileToPNGFile(file, filenameWithExtension);

		// 모델에 이미지 파일 경로를 추가하여 뷰로 전달
		model.addAttribute("file", filePath);

		userPlusForm.setId(userId);
		userPlusForm.setProfileName(filenameWithExtension);
		
		userService.updateUser(UserConverter.userPlusForm(userPlusForm));

		return "redirect:/mypage";
	}

	// 이미지 출력을 위한 매서드
	@GetMapping("/show")
	public ResponseEntity<Resource> display(@RequestParam("profileName") String profileName) {
		String folder = "";
		Resource resource = new FileSystemResource(path + folder + profileName);
		if (!resource.exists())
			return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		HttpHeaders header = new HttpHeaders();
		Path filePath = null;
		try {
			filePath = Paths.get(path + folder + profileName);
			header.add("Content-type", Files.probeContentType(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
	}

}
