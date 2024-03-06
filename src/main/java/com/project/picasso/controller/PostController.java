package com.project.picasso.controller;

import java.awt.image.BufferedImage;
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
import com.project.picasso.model.posts.PostConverter;
import com.project.picasso.model.posts.PostEditForm;
import com.project.picasso.model.users.User;
import com.project.picasso.oauth.ValidatedUser;
import com.project.picasso.service.PostService;
import com.project.picasso.service.UserService;
import com.project.picasso.util.FileService;
import com.project.picasso.util.PageNavigator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Controller
public class PostController {

	private final WordCloudService wordCloudService;
	private final FileService fileService;
	private final PostService postService;
	private final UserService userService;
	private int countPerPage = 6;
	private int pagePerGroup = 5;

	@Value("${file.upload.path}")
	private String uploadPath;
	
	private String path = "C:\\workspace\\upload\\";
	
	// 이미지 생성
	@GetMapping("imageGenerate")
	public String imageGenerateForm() {
		return "/imageGenerate";
	}

	@PostMapping("imageGenerate")
	public String imageGenerate(@RequestParam(name = "writing") String writing, Model model,
								Authentication authentication) {
		
		ValidatedUser loginUser = (ValidatedUser) authentication.getPrincipal();
		log.info("밸리데이티드 유저는: {}", loginUser);
		
		String sentence = writing;
		BufferedImage wordCloudImage = wordCloudService.generateWordCloud(sentence);
	
		// 파일명 생성
		String filenameWithExtension = fileService.generateUniqueFilename() + ".png";

		
		// 이미지를 서버에 저장하고 지정된 파일명을 반환받음
		String filePath = fileService.saveImageToPNGFile(wordCloudImage, filenameWithExtension);

		// 모델에 이미지 파일 경로를 추가하여 뷰로 전달
		model.addAttribute("wordCloudImage", filePath);
		log.info("파일 패스: {}", filePath);

		Post post = new Post();
		post.setFileName(filenameWithExtension);
		post.setContent(writing);
		post.setUser(loginUser.getUser());
		post.setHash(CloudImageGenerator.one);
		post.setHash2(CloudImageGenerator.two);
		post.setHash3(CloudImageGenerator.thr);

		postService.savePost(post);

		CloudImageGenerator.one=null;
		CloudImageGenerator.two=null;
		CloudImageGenerator.thr=null;
		
		return "redirect:/list";
	}


	// 글 전체보기
	@GetMapping("list")
	public String listPosts(Authentication authentication,
			@RequestParam(name = "page", defaultValue = "1") int page,
							Model model) {
		ValidatedUser loginUser = (ValidatedUser) authentication.getPrincipal();
		
	    User user = userService.findUserById(loginUser.getUser().getId());
	    int total = postService.getUserTotal(user.getId());
		PageNavigator navi = new PageNavigator(countPerPage, pagePerGroup, page, total);
	    log.info("navi:"+navi);		
	    List<Post> posts = userService.getIdListPost(user.getId(),navi.getStartRecord(), navi.getCountPerPage());
	    model.addAttribute("userposts", posts);
	    model.addAttribute("navi",navi);
	    
	    return "/list";  
	}


	// 게시글 상세 조회
	@GetMapping("view/{postId}")
	public String viewPost(@PathVariable(name = "postId") Long postId, Model model) {
		Post post = postService.readPost(postId);
		model.addAttribute("post", post);
		log.info("post: {}", model);
		return "/view";
	}

	// 게시글 삭제
	@GetMapping("view/remove/{postId}")
	public String removePost(Authentication authentication,
							@PathVariable(name = "postId") Long postId) {
		
		ValidatedUser loginUser = (ValidatedUser) authentication.getPrincipal();
		// 해당 ID의 게시글 가져오기
	    Post post = postService.getPostById(postId);
	    
	    // 게시글이 존재하는지 확인
	    if (post != null) {
	        // 게시글 삭제
	        postService.removePost(postId, loginUser.getUser());
	        
	        // 게시글에 연결된 이미지 파일 삭제
	        String filename = post.getFileName();
	        if (filename != null && !filename.isEmpty()) {
	            String folder = ""; // 필요에 따라 폴더 경로 설정
	            Path imagePath = Paths.get(path + folder + filename);

	            try {
	                Files.deleteIfExists(imagePath); // 이미지 파일 삭제 시도
	                log.info("이미지 파일 삭제: {}", imagePath);
	            } catch (IOException e) {
	                log.error("이미지 파일 삭제 중 오류 발생: {}", e.getMessage());
	            }
	        }
	        
	        return "redirect:/list";
	    } else {
	        // 게시글이 존재하지 않는 경우 처리
	        // 적절한 에러 핸들링을 수행하거나 리다이렉트 등을 수행할 수 있습니다.
	        return "redirect:/error";
	    }
	    
	}

	// 게시글 수정 폼
	@GetMapping("edit/{postId}")
	public String editPostForm(Authentication authentication,
								@PathVariable(name = "postId") Long postId, Model model) {
		Post post = postService.getPostById(postId);
		ValidatedUser loginUser = (ValidatedUser) authentication.getPrincipal();
		if (post == null || !post.getUser().getId().equals(loginUser.getUser().getId())) {
			log.info("수정 권한 없음");
			return "redirect:/list";
		}

		PostEditForm postEditForm = PostConverter.postToPostEditForm(post);
		model.addAttribute("postEditForm", postEditForm);
		return "/edit";
	}
	
	// 게시글 수정
	   @PostMapping("edit/{postId}")
	   public String editPost(Authentication authentication,
			   				  @PathVariable(name = "postId") Long postId,
			   				  @RequestParam(name = "content", required = false) String content,
			   				  Model model) {
		   Post findPost = postService.getPostById(postId);
		   ValidatedUser loginUser = (ValidatedUser) authentication.getPrincipal();
		   if (findPost == null || !findPost.getUser().getId().equals(loginUser.getUser().getId())) {
			   log.info("수정 권한 없음");
			   return "redirect:/list";
		   }

		   String sentence = content;
		   BufferedImage wordCloudImage = wordCloudService.generateWordCloud(sentence);

			// 파일명 생성
		   String filenameWithExtension = fileService.generateUniqueFilename() + ".png";

			
		   // 이미지를 서버에 저장하고 지정된 파일명을 반환받음
		   String filePath = fileService.saveImageToPNGFile(wordCloudImage, filenameWithExtension);

		   // 모델에 이미지 파일 경로를 추가하여 뷰로 전달
		   model.addAttribute("wordCloudImage", filePath);
		   log.info("파일 패스: {}", filePath);
		   
//	      postEditForm.setId(postId);
//	      
//	      Post post = PostConverter.postEditFormToPost(postEditForm);
		   
		   Post post = new Post();
		   
		   post.setId(postId);
		   post.setFileName(filenameWithExtension);
		   post.setContent(content);
		   post.setUser(loginUser.getUser());
		   
		   
		   postService.updatePost(post);

	      return "redirect:/list";
	   }
	   
	   
	   //이미지 출력을 위한 매서드 
	   @GetMapping("/display")
	   public ResponseEntity<Resource> display(@RequestParam("filename") String filename) {
	   	String folder = "";
	   	Resource resource = new FileSystemResource(path + folder + filename);
	   	if(!resource.exists()) 
	   		return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
	   	HttpHeaders header = new HttpHeaders();
	   	Path filePath = null;
	   	try{
	   		filePath = Paths.get(path + folder + filename);
	   		header.add("Content-type", Files.probeContentType(filePath));
	   	}catch(IOException e) {
	   		e.printStackTrace();
	   	}
	   	return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
	   }

}
