package Spring.Practice.service;

import Spring.Practice.domain.item.Album;
import Spring.Practice.repository.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceTest {

	@Autowired private ItemService itemService;
	@Autowired private ItemRepository itemRepository;

	@Test
	@DisplayName("상품 저장")
	void save() throws Exception {
		// given
		Album album = new Album();
		album.setName("앨범");
		album.setArtist("홍길동");

		// when
		Long id = itemService.save(album);

		// then
		Assertions.assertThat(album).isEqualTo(itemRepository.findOne(id));
	}
}