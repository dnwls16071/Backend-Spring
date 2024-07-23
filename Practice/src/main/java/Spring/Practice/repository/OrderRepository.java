package Spring.Practice.repository;

import Spring.Practice.domain.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

	@PersistenceContext
	private EntityManager em;

	public void save(Order order) {
		em.persist(order);
	}

	public Order findOne(Long orderId) {
		return em.find(Order.class, orderId);
	}
}
