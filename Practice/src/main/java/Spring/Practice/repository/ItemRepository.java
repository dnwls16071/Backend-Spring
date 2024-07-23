package Spring.Practice.repository;

import Spring.Practice.domain.item.Item;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemRepository {

	@PersistenceContext
	private EntityManager em;

	public Long saveItem(Item item) {
		if (item.getId() != null) {
			em.merge(item);
		} else {
			em.persist(item);
		}
		return item.getId();
	}

	public Item findOne(Long itemId) {
		return em.find(Item.class, itemId);
	}

	public List<Item> findAll() {
		return em.createQuery("select i from Item i", Item.class)
				.getResultList();
	}
}
