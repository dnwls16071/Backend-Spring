package hello.upload.file;

import hello.upload.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Component
public class FileStore {

	@Value("${file.dir}")
	private String fileDir;

	public String getFullPath(String fileName) {
		// C:/Users/TOP/ + ...
		return fileDir + fileName;
	}

	// 복수 파일
	public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
		List<UploadFile> storeFileResult = new ArrayList<>();
		for (MultipartFile multipartFile : multipartFiles) {
			if (!multipartFile.isEmpty()) {
				storeFileResult.add(storeFile(multipartFile));
			}
		}
		return storeFileResult;
	}


	// 단일 파일
	public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
		if (multipartFile.isEmpty()) {
			return null;
		}
		String originalFilename = multipartFile.getOriginalFilename();

		// 확장자 확인
		int i = originalFilename.lastIndexOf(".");
		String ext = originalFilename.substring(i + 1);

		// 서로 다른 고객이 같은 이름의 이미지를 업로드 시 덮어씌여지는 문제가 발생
		// 이를 해결하기 위해 UUID 값을 활용
		String uuid = UUID.randomUUID().toString();

		// 서버에 저장하는 파일명
		String storeFileName = uuid + "." + ext;
		multipartFile.transferTo(new File(getFullPath(storeFileName)));
		return new UploadFile(originalFilename, storeFileName);
	}
}
