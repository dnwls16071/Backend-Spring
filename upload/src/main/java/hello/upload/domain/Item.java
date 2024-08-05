package hello.upload.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Item {
	private Long id;
	private String itemName;
	private UploadFile attachFile;			// 첨부파일 하나
	private List<UploadFile> imageFiles;	// 첨부파일 여러 개
}
