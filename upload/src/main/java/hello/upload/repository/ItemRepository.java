package hello.upload.repository;

import hello.upload.domain.Item;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ItemRepository {

	private final Map<Long, Item> store = new HashMap<>();
	private long sequence = 0L;

	// 아이템 저장
	public Item save(Item item) {
		item.setId(++sequence);
		store.put(item.getId(), item);
		return item;
	}

	// ID로 아이템 찾기
	public Item findById(Long id) {
		Item item = store.get(id);
		return item;
	}
}
