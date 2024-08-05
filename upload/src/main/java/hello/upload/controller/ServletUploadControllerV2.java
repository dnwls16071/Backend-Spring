package hello.upload.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/servlet/v2")
public class ServletUploadControllerV2 {

	// application.properties에 설정한 옵션을 사용
	@Value("${file.dir}")
	private String fileDir;

	@GetMapping("/upload")
	public String newFile() {
		return "upload-form";
	}

	@PostMapping("/upload")
	public String saveFileV2(HttpServletRequest request) throws ServletException, IOException {
		log.info("request={}", request);

		String itemName = request.getParameter("itemName");
		log.info("itemName={}", itemName);

		Collection<Part> parts = request.getParts();
		log.info("parts={}", parts);

		for (Part part : parts) {
			log.info("==== PART ====");
			log.info("name={}", part.getName());
			Collection<String> headerNames = part.getHeaderNames();
			for (String headerName : headerNames) {
				log.info("header {} : {}", headerName, part.getHeader(headerName));
			}

			// 클라이언트가 전달한 파일명
			log.info("submittedFileName={}", part.getSubmittedFileName());
			log.info("size={}", part.getSize());

			// Part의 전송 데이터 읽기
			InputStream inputStream = part.getInputStream();
			String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
			log.info("body={}", body);

			// Part를 통해 전송된 데이터를 저장
			// 클라이언트가 전달한 파일명이 존재한다면?
			if (StringUtils.hasText(part.getSubmittedFileName())) {
				String fullPath = fileDir + part.getSubmittedFileName();
				log.info("파일 저장 경로 fullPath={}", fullPath);
				part.write(fullPath);
			}
		}
		return "upload-form";
	}
}
