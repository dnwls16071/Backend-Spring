package Spring.Practice.domain;

import Spring.Practice.domain.item.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "orderitem_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;

	private int orderPrice;

	private int count;

	// 주문상품 엔티티에 비즈니스 로직 추가(주문이 접수되면 주문상품을 넣을 수 있도록)
	public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
		OrderItem orderItem = new OrderItem();
		orderItem.setItem(item);
		orderItem.setOrderPrice(orderPrice);
		orderItem.setCount(count);
		item.removeStock(count);
		return orderItem;
	}

	// 주문상품 엔티티에 비즈니스 로직 추가(주문 취소 시 재고 수량에 다시 반영하도록)
	public void cancel() {
		getItem().addStock(count);
	}

	// 주문상품 전체 가격 조회
	public int getTotalPrice() {
		return orderPrice * count;
	}
}
