package com.kycassignment.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {

	public String uploadFile(MultipartFile file, int userId, int docId) throws IllegalStateException, IOException {

		SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
		Date date = new Date();
		System.out.println(formatter.format(date));
		String ext;
		if(file.getContentType().equals("image/png")) {
			ext=".png";
		} else {
			ext=".jpg";
		}
		String url = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\uploadedfiles\\"+userId+"_"+docId+"_"+formatter.format(date)+ext;

		file.transferTo(new File(url));
		url=userId+"_"+docId+"_"+formatter.format(date)+ext;
		System.out.println("successfully uploaded ----------------->");

		return url;
	}
}
//		file.transferTo(new File(System.getProperty("user.dir")+"\\src\\main\\resources\\static\\uploadedfiles\\"+file.getOriginalFilename()));
