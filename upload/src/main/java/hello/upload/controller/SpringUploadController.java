package hello.upload.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/spring")
public class SpringUploadController {

	// application.properties에 설정한 옵션을 사용
	@Value("${file.dir}")
	private String fileDir;

	@GetMapping("/upload")
	public String newFile() {
		return "upload-form";
	}

	@PostMapping("/upload")
	public String saveFileV2(@RequestParam(name = "itemName") String itemName,
							 @RequestParam(name = "file") MultipartFile file,
							 HttpServletRequest request) throws IOException {
		log.info("request={}", request);
		log.info("itemName={}", itemName);
		log.info("multiPartFile={}", file);

		// 파일이 비어있지 않다면?
		if (!file.isEmpty()) {
			// 업로드 파일명 : getOriginalFilename()
			String fullPath = fileDir + file.getOriginalFilename();
			log.info("파일 저장 경로 fullPath={}", fullPath);
			// 파일 저장 : transferTo()
			file.transferTo(new File(fullPath));
		}
		return "upload-form";
	}
}
