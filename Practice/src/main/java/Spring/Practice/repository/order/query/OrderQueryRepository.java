package Spring.Practice.repository.order.query;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	public List<OrderQueryDto> findAllByDto_optimization() {
		List<OrderQueryDto> result = findOrders();

		// findOrders() → order의 식별자 orderId
		// orderId 리스트
		List<Long> orderIds = result.stream()
				.map(o -> o.getOrderId())
				.collect(Collectors.toList());

		// 파라미터에 orderId리스트를 넣고 IN절 사용
		List<OrderItemQueryDto> orderItems = em.createQuery("select new Spring.Practice.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) from OrderItem oi join oi.item i where oi.order.id in :orderIds", OrderItemQueryDto.class)
				.setParameter("orderIds", orderIds)
				.getResultList();

		// OrderItemQueryDto → (Long orderId, String itemName, int orderPrice, int count)
		// 식별자 orderId를 가져와서 메모리 상에서 그룹화
		// 그렇게 되면 하나의 orderId에 있는 OrderItem 컬렉션이 담기게 되고 orderId가 key, 컬렉션이 value가 됨
		Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream().collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
		result.forEach(o -> o.setOrderItemQueryDtoList(orderItemMap.get(o.getOrderId())));
		return result;
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
