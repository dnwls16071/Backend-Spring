package hello.upload.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemForm {
	private Long itemId;
	private String itemName;
	private MultipartFile attachFile;
	private List<MultipartFile> imageFiles;
}
