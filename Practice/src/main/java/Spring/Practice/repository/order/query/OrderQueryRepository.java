package Spring.Practice.repository.order.query;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

// 엔티티가 아닌 다른 것들을 조회하기 위한 별도의 리포지토리
@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

	private final EntityManager em;

	public List<OrderQueryDto> findOrderQueryDtos() {
		// [1]. 컬렉션을 제외한 DTO 가져오기
		// 이 때, OrderQueryDTO에는 private List<OrderItemQueryDto> orderItemQueryDtoList;가 존재
		List<OrderQueryDto> result = findOrders();
		// [2]. 루프를 돌리면서 컬렉션 OrderItemQueryDto 만들기
		result.stream().forEach(orderQueryDto -> {
			List<OrderItemQueryDto> orderItems = findOrderItems(orderQueryDto.getOrderId());
			orderQueryDto.setOrderItemQueryDtoList(orderItems);
		});
		return result;
	}

	// ORDERITEM : ITEM = N : 1
	// [2]. ToMany 관계는 이와 같이 별도로 처리
	// private List<OrderItem> orderItems = new ArrayList<>();
	// ORDERITEM → 내부 요소들에 대한 DTO(id, item, order, orderPrice, count)
	private List<OrderItemQueryDto> findOrderItems(Long orderId) {
		return em.createQuery("select new Spring.Practice.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) from OrderItem oi join oi.item i where oi.order.id = :orderId", OrderItemQueryDto.class)
				.setParameter("orderId", orderId)
				.getResultList();
	}
	
	// [1]. ToOne 관계들을 먼저 처리
	// ORDER : MEMBER = N : 1
	// ORDER : DELIVERY = 1 : 1
	// 이 때, 컬렉션은 포함시키지 않는다.
	private List<OrderQueryDto> findOrders() {
		return em.createQuery("select new Spring.Practice.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address) from Order o join o.member m join o.delivery d", OrderQueryDto.class)
				.getResultList();
	}
}
