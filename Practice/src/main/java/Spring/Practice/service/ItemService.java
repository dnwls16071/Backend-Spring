package Spring.Practice.service;

import Spring.Practice.domain.item.Item;
import Spring.Practice.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ItemService {

	private final ItemRepository itemRepository;
	@Autowired
	public ItemService(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	@Transactional
	public Long save(Item item) {
		Long id = itemRepository.saveItem(item);
		return id;
	}

	public Item findOne(Long itemId) {
		return itemRepository.findOne(itemId);
	}

	public List<Item> findItems() {
		return itemRepository.findAll();
	}
}
