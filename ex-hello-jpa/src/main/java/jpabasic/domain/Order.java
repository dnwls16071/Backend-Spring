package jpabasic.domain;

import jakarta.persistence.*;
import jpabasic.domain.baseentity.BaseEntity;
import jpabasic.domain.enumType.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "ORDERS")
public class Order extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private Long id;
	private LocalDateTime orderDate;
	private OrderStatus status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	// 주문 엔티티를 관리함에 따라 주문상품 엔티티 역시 같이 관리됨
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderItem> orderItems = new ArrayList<>();

	// 주문 엔티티를 관리함에 따라 배송 엔티티 역시 같이 관리됨
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "delivery_id")
	private Delivery delivery;

	public void addOrderItem(OrderItem orderItem) {
		orderItem.setOrder(this);
		this.getOrderItems().add(orderItem);
	}
}
