package hello.upload.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UploadFile {
	private String uploadFileName; // 업로드한 파일명
	private String storeFileName;  // 서버에 저장할 파일명
}
