package hello.upload.controller;

import hello.upload.domain.Item;
import hello.upload.domain.UploadFile;
import hello.upload.file.FileStore;
import hello.upload.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Controller
public class ItemController {

	private final ItemRepository itemRepository;
	private final FileStore fileStore;

	@Autowired
	public ItemController(ItemRepository itemRepository, FileStore fileStore) {
		this.itemRepository = itemRepository;
		this.fileStore = fileStore;
	}

	@GetMapping("/items/new")
	public String itemForm(@ModelAttribute(name = "form") ItemForm form) {
		return "item-form";
	}

	@PostMapping("/items/new")
	public String itemSave(@ModelAttribute(name = "form") ItemForm form, RedirectAttributes redirectAttributes) throws IOException {
		UploadFile attachFile = fileStore.storeFile(form.getAttachFile());
		List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());

		Item item = Item.builder()
						.id(form.getItemId())
						.itemName(form.getItemName())
						.attachFile(attachFile)
						.imageFiles(storeImageFiles)
						.build();
		itemRepository.save(item);
		redirectAttributes.addAttribute("itemId", item.getId());
		return "redirect:/items/{itemId}";
	}

	// 저장된 파일을 고객에게 보여주는 매핑
	@GetMapping("/items/{id}")
	public String items(@PathVariable(name = "id") Long id, Model model) {
		Item findItem = itemRepository.findById(id);
		model.addAttribute("item", findItem);
		return "item-view";
	}

	// java.net.MalformedURLException: unknown protocol: c (오류)
	@ResponseBody
	@GetMapping("/images/{fileName}")
	public Resource downloadImageV1(@PathVariable(name = "fileName") String fileName) throws MalformedURLException {
		// UrlResource wraps a java.net.URL and can be used to access any object that is normally accessible with a URL, such as files, an HTTPS target, an FTP target, and others.
		// All URLs have a standardized String representation, such that appropriate standardized prefixes are used to indicate one URL type from another.
		// This includes file: for accessing filesystem paths, https: for accessing resources through the HTTPS protocol, ftp: for accessing resources through FTP, and others.
		return new UrlResource("file:" + fileStore.getFullPath(fileName));
	}

	// 첨부 파일 클릭하여 다운로드
	@GetMapping("/attach/{itemId}")
	public ResponseEntity<Resource> downloadImageV2(@PathVariable(name = "itemId") Long itemId) throws MalformedURLException {
		Item item = itemRepository.findById(itemId);
		String storeFileName = item.getAttachFile().getStoreFileName();
		String uploadFileName = item.getAttachFile().getUploadFileName();
		UrlResource urlResource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));

		// 인코딩
		String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);

		// header : Content-Disposition
		String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
				.body(urlResource);
	}
}
