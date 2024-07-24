package Spring.Practice.api;

import Spring.Practice.domain.Address;
import Spring.Practice.domain.Order;
import Spring.Practice.domain.OrderItem;
import Spring.Practice.domain.enumType.OrderStatus;
import Spring.Practice.repository.OrderRepository;
import Spring.Practice.repository.order.query.OrderQueryDto;
import Spring.Practice.repository.order.query.OrderQueryRepository;
import Spring.Practice.service.OrderService;
import Spring.Practice.util.OrderSearch;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderApiController {

	private final OrderService orderService;
	private final OrderQueryRepository orderQueryRepository;
	private final OrderRepository orderRepository;

	@Autowired
	public OrderApiController(OrderService orderService, OrderQueryRepository orderQueryRepository, OrderRepository orderRepository) {
		this.orderService = orderService;
		this.orderQueryRepository = orderQueryRepository;
		this.orderRepository = orderRepository;
	}

	// 지연 로딩으로 인한 너무 많은 SQL 실행이 문제가 됨
	// 컬렉션도 DTO로 별도로 만들어주어야 한다.
	// Ex. OrderItems → OrderItemDto
	@GetMapping("/api/v2/orders")
	public List<OrderDto> ordersV2() {
		List<Order> all = orderRepository.findAllByString(new OrderSearch());
		return all.stream()
				.map(order -> new OrderDto(order))
				.collect(Collectors.toList());
	}

	// 페치 조인 적용
	// "select distinct o from Order o join fetch o.member m join fetch o.delivery d join fetch o.orderItems oi join fetch oi.item i"
	// 페치 조인으로 SQL 1번만 실행됨
	// distinct를 사용하는 이유는 애플리케이션에서 중복을 걸러준다.
	// 페이징 불가능
	@GetMapping("/api/v3/orders")
	public List<OrderDto> ordersV3() {
		List<Order> all = orderRepository.findAllWithItem();
		return all.stream()
				.map(order -> new OrderDto(order))
				.collect(Collectors.toList());
	}

	@GetMapping("/api/v3.1/orders")
	public List<OrderDto> ordersV3page(
			@RequestParam(name = "offset", defaultValue = "0") int offset,
			@RequestParam(name = "limit", defaultValue = "100") int limit
	) {
		List<Order> all = orderRepository.findAllWithMemberDelivery(offset, limit);
		return all.stream()
				.map(order -> new OrderDto(order))
				.collect(Collectors.toList());
	}

	@GetMapping("/api/v4/orders")
	public List<OrderQueryDto> ordersV4() {
		return orderQueryRepository.findOrderQueryDtos();
	}

	@Data
	static class OrderDto {
		private Long orderId;
		private String name;
		private LocalDateTime orderDate;
		private OrderStatus orderStatus;
		private Address address;
		private List<OrderItemDto> orderItems;

		public OrderDto(Order order) {
			orderId = order.getId();
			name = order.getMember().getName();
			orderDate = order.getOrderDate();
			orderStatus = order.getStatus();
			address = order.getDelivery().getAddress();
			orderItems = order.getOrderItems()
					.stream()
					.map(orderItem -> new OrderItemDto(orderItem))
					.collect(Collectors.toList());
		}
	}

	@Getter
	static class OrderItemDto {
		private String itemName; // 상품명
		private int orderPrice;	 // 주문 가격
		private int count;		 // 주문 수량
		
		public OrderItemDto(OrderItem orderItem) {
			itemName = orderItem.getItem().getName();
			orderPrice = orderItem.getOrderPrice();
			count = orderItem.getCount();
		}
	}
}
