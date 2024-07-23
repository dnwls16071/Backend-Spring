package Spring.Practice.repository;

import Spring.Practice.domain.Order;
import Spring.Practice.util.OrderSearch;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

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

	// 검색 기능 추가(JPQL)
	public List<Order> findAllByString(OrderSearch orderSearch) {
		String query = "select o from Order o join o.member m";
		boolean isFirstCondition = true;

		// 주문 상태로 검색하는 경우
		if (orderSearch.getOrderStatus() != null) {
			if (isFirstCondition) {
				query += " where";
				isFirstCondition = false;
			} else {
				query += "and";
			}
			query += " o.status = :status";
		}

		// 회원 이름으로 검색하는 경우
		if (orderSearch.getMemberName() != null) {
			if (isFirstCondition) {
				query += " where";
				isFirstCondition = false;
			} else {
				query += " and";
			}
			query += " m.name like :name";
		}

		TypedQuery<Order> result = em.createQuery(query, Order.class).setMaxResults(1000);

		if (orderSearch.getOrderStatus() != null) {
			result = result.setParameter("status", orderSearch.getOrderStatus());
		}

		if (StringUtils.hasText(orderSearch.getMemberName())) {
			result = result.setParameter("name", orderSearch.getMemberName());
		}
		return result.getResultList();
	}
}
