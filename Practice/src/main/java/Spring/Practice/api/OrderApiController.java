package Spring.Practice.api;

import Spring.Practice.domain.Address;
import Spring.Practice.domain.Order;
import Spring.Practice.domain.enumType.OrderStatus;
import Spring.Practice.repository.OrderRepository;
import Spring.Practice.service.OrderService;
import Spring.Practice.util.OrderSearch;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderApiController {

	private final OrderRepository orderRepository;
	@Autowired
	public OrderApiController(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@GetMapping("/api/v2/simple-orders")
	public Result ordersV1() {
		List<Order> all = orderRepository.findAllByString(new OrderSearch());
		List<SimpleOrderDTO> collect = all.stream()
				.map(order -> new SimpleOrderDTO(order))
				.collect(Collectors.toList());
		return new Result(collect);
	}

	@Data
	static class SimpleOrderDTO {
		private Long orderId;
		private String name;
		private LocalDateTime orderDate;
		private OrderStatus orderStatus;
		private Address address;

		// LAZY(지연로딩) → 프록시(객체)
		// 해당 엔티티의 메서드를 호출하는 순간 실제 메서드가 됨
		public SimpleOrderDTO(Order order) {
			orderId = order.getId();
			name = order.getMember().getName();			// 여기서 한 번!
			orderDate = order.getOrderDate();
			orderStatus = order.getStatus();
			address = order.getDelivery().getAddress();	// 여기서 한 번!
		}
	}

	@Data
	static class Result<T> {
		private T value;

		public Result(T value) {
			this.value = value;
		}
	}
}
