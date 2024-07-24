package Spring.Practice.api;

import Spring.Practice.domain.Address;
import Spring.Practice.domain.Order;
import Spring.Practice.domain.OrderItem;
import Spring.Practice.domain.enumType.OrderStatus;
import Spring.Practice.repository.OrderRepository;
import Spring.Practice.service.OrderService;
import Spring.Practice.util.OrderSearch;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderApiController {

	private final OrderService orderService;
	private final OrderRepository orderRepository;

	@Autowired
	public OrderApiController(OrderService orderService, OrderRepository orderRepository) {
		this.orderService = orderService;
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
