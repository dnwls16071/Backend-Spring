package Spring.Practice.service;

import Spring.Practice.domain.Delivery;
import Spring.Practice.domain.Member;
import Spring.Practice.domain.Order;
import Spring.Practice.domain.OrderItem;
import Spring.Practice.domain.enumType.DeliveryStatus;
import Spring.Practice.domain.item.Item;
import Spring.Practice.repository.ItemRepository;
import Spring.Practice.repository.MemberRepository;
import Spring.Practice.repository.OrderRepository;
import Spring.Practice.util.OrderSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {

	private final OrderRepository orderRepository;
	private final ItemRepository itemRepository;
	private final MemberRepository memberRepository;

	@Autowired
	public OrderService(OrderRepository orderRepository, ItemRepository itemRepository, MemberRepository memberRepository) {
		this.orderRepository = orderRepository;
		this.itemRepository = itemRepository;
		this.memberRepository = memberRepository;
	}

	@Transactional
	public Long order(Long memberId, Long itemId, int count) {
		Member member = memberRepository.findOne(memberId);
		Item item = itemRepository.findOne(itemId);

		Delivery delivery = new Delivery();
		delivery.setAddress(member.getAddress());
		delivery.setDeliveryStatus(DeliveryStatus.READY);

		// 주문 상품 생성
		OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

		// 주문 생성
		Order order = Order.createOrder(member, delivery, orderItem);

		// 주문 저장
		orderRepository.save(order);
		return order.getId();
	}

	@Transactional
	public void cancelOrder(Long orderId) {
		Order order = orderRepository.findOne(orderId);
		order.cancelOrder();
	}

	// 검색 기능
	public List<Order> findOrders(OrderSearch orderSearch) {
		return orderRepository.findAllByString(orderSearch);
	}
}
