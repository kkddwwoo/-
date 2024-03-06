package com.project.picasso.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.NoArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.OutputStream;
import java.io.FileOutputStream;

@Service
public class FileService {

	@Value("${file.upload.path}")
	private String uploadPath;

	public String generateUniqueFilename() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sdf.format(new Date());
	}

	public String saveImageToPNGFile(BufferedImage image, String filenameWithExtension) {
		try {

//            // 파일 경로 생성
//            String filePath = uploadPath + File.separator + filename + ".png";
//            
//            // 이미지 저장
//            File outputFile = new File(filePath);
//            ImageIO.write(image, "png", outputFile);
//        	

			// 파일 경로 생성
			String filePath = uploadPath + File.separator + filenameWithExtension;

			// 이미지 저장
			File outputFile = new File(filePath);
			String extension = filenameWithExtension.substring(filenameWithExtension.lastIndexOf(".") + 1);
			ImageIO.write(image, extension, outputFile);

			return filePath;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 파일 이미지 저장
	public String saveProfileToPNGFile(MultipartFile file, String filenameWithExtension) {
		try {

			// 파일 경로 생성
			String filePath = uploadPath + File.separator + filenameWithExtension;

			// 이미지 저장
			File outputFile = new File(filePath);
			String extension = filenameWithExtension.substring(filenameWithExtension.lastIndexOf(".") + 1);

			// MultipartFile을 File로 변환하여 저장
			try (OutputStream os = new FileOutputStream(outputFile)) {
				os.write(file.getBytes());
			}
			return filePath;
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
