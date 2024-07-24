package Spring.Practice.api;

import Spring.Practice.domain.Address;
import Spring.Practice.domain.Order;
import Spring.Practice.domain.enumType.OrderStatus;
import Spring.Practice.repository.OrderRepository;
import Spring.Practice.util.OrderSearch;
import Spring.Practice.util.SimpleOrderQueryDTO;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderSimpleApiController {

	private final OrderRepository orderRepository;
	@Autowired
	public OrderSimpleApiController(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@GetMapping("/api/v2/simple-orders")
	public Result ordersV2() {
		List<Order> all = orderRepository.findAllByString(new OrderSearch());
		List<SimpleOrderDTO> collect = all.stream()
				.map(order -> new SimpleOrderDTO(order))
				.collect(Collectors.toList());
		return new Result(collect);
	}

	// 기본적으로는 LAZY Loading(지연 로딩)
	// 필요한 것들만 객체 그래프로 탐색하여 한 번에 가져오도록 하는 것(페치 조인)
	@GetMapping("/api/v3/simple-orders")
	public List<SimpleOrderDTO> ordersV3() {
		List<Order> all = orderRepository.findAllWithMemberDelivery();
		return all.stream()
				.map(order -> new SimpleOrderDTO(order))
				.collect(Collectors.toList());
	}

	@GetMapping("/api/v4/simple-orders")
	public List<SimpleOrderQueryDTO> ordersV4() {
		return orderRepository.findOrderDtos();
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
