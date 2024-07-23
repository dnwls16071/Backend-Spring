package Spring.Practice.domain;

import Spring.Practice.domain.enumType.DeliveryStatus;
import Spring.Practice.domain.enumType.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "ORDERS")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	private LocalDateTime orderDate;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "delivery_id")
	private Delivery delivery;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> orderItems = new ArrayList<>();

	// 연관관계 편의 메서드(Order - OrderItems)
	public void addOrderItem(OrderItem orderItem) {
		orderItem.setOrder(this);
		this.getOrderItems().add(orderItem);
	}

	// 주문 엔티티에 비즈니스 로직 추가(주문 생성)
	public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
		Order order = new Order();
		order.setMember(member);
		order.setDelivery(delivery);
		for (OrderItem orderItem : orderItems) {
			order.addOrderItem(orderItem);
		}
		order.setOrderDate(LocalDateTime.now());
		order.setStatus(OrderStatus.ORDER);
		return order;
	}

	// 주문 엔티티에 비즈니스 로직 추가(주문 취소)
	public void cancelOrder() {
		if (delivery.getDeliveryStatus() == DeliveryStatus.COMP) {
			throw new IllegalStateException("배송완료된 상품은 주문 취소가 불가합니다.");
		}
		this.setStatus(OrderStatus.CANCEL);
		for (OrderItem orderItem : orderItems) {
			orderItem.cancel();
		}
	}

	// 한 주문 내에 담긴 주문상품 전체 가격을 구하는 메서드
	// 주문상품 단가 * 개수
	public int getTotalPrice() {
		int totalPrice = 0;
		for (OrderItem orderItem : orderItems) {
			totalPrice += orderItem.getOrderPrice() * orderItem.getCount();
		}
		return totalPrice;
	}
}
