package Spring.Practice.controller;

import Spring.Practice.domain.item.Book;
import Spring.Practice.domain.item.Item;
import Spring.Practice.service.ItemService;
import Spring.Practice.util.ItemForm;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@Slf4j
public class ItemController {
	private final ItemService itemService;
	@Autowired
	public ItemController(ItemService itemService) {
		this.itemService = itemService;
	}

	// 상품 등록 Get
	@GetMapping("/items/new")
	public String createForm(Model model) {
		log.info("createForm Controller(GET)");
		model.addAttribute("form", new ItemForm());
		return "items/createItemForm";
	}

	// 상품 등록 Post
	@PostMapping("/items/new")
	public String create(@Valid @ModelAttribute(name = "form") ItemForm form, BindingResult bindingResult) {
		log.info("create Controller(POST)");
		// 바인딩 오류 발생 시 → 회원 가입 페이지로 다시 보내기
		if (bindingResult.hasErrors()) {
			return "items/createItemForm";
		}
		Book book = new Book();
		book.setName(form.getName());
		book.setPrice(form.getPrice());
		book.setStockQuantity(form.getStockQuantity());
		book.setAuthor(form.getAuthor());
		book.setIsbn(form.getIsbn());
		itemService.save(book);
		return "redirect:/items";
	}

	// 상품 목록 조회 GET
	@GetMapping("/items")
	public String list(Model model) {
		log.info("list Controller(GET)");
		List<Item> items = itemService.findItems();
		model.addAttribute("items", items);
		return "items/itemList";
	}

	// 상품 수정 GET
	@GetMapping("/items/{id}/edit")
	public String updateForm(@PathVariable(name = "id") Long id, Model model) {
		log.info("updateForm Controller(GET)");
		Book book = (Book) itemService.findOne(id);
		ItemForm itemForm = new ItemForm();
		itemForm.setId(book.getId());
		itemForm.setStockQuantity(book.getStockQuantity());
		itemForm.setPrice(book.getPrice());
		itemForm.setName(book.getName());
		itemForm.setIsbn(book.getIsbn());
		itemForm.setAuthor(book.getAuthor());
		model.addAttribute("form", itemForm);
		return "items/updateItemForm";
	}

	// 상품 수정 POST
	@PostMapping("/items/{id}/edit")
	public String update(@ModelAttribute(name = "form") ItemForm form) {
		log.info("update Controller(POST)");
		Book book = new Book();
		book.setId(form.getId());
		book.setStockQuantity(form.getStockQuantity());
		book.setPrice(form.getPrice());
		book.setName(form.getName());
		book.setIsbn(form.getIsbn());
		book.setAuthor(form.getAuthor());
		itemService.save(book);
		return "redirect:/items";
	}
}
