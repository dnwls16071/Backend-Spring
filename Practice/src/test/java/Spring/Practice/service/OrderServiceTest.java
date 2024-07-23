package Spring.Practice.service;

import Spring.Practice.domain.Address;
import Spring.Practice.domain.Member;
import Spring.Practice.domain.Order;
import Spring.Practice.domain.enumType.OrderStatus;
import Spring.Practice.domain.item.Book;
import Spring.Practice.exception.NotEnoughStockException;
import Spring.Practice.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

	@PersistenceContext
	private EntityManager em;

	@Autowired private OrderService orderService;
	@Autowired private OrderRepository orderRepository;

	@Test
	@DisplayName("주문 생성 테스트")
	void createOrder() throws Exception {
		// given
		Member member = createMember();
		Book book = createBook("책1", 10000, 50);
		int orderCount = 2;

		// when
		Long id = orderService.order(member.getId(), book.getId(), orderCount);

		// then
		Order findOrder = orderRepository.findOne(id);
		// 주문이 생성되면 주문 상태는 ORDER이 되어야 한다.
		Assertions.assertThat(findOrder.getStatus()).isEqualTo(OrderStatus.ORDER);
		// 주문한 상품 종류의 수가 정확해야 한다.
		Assertions.assertThat(findOrder.getOrderItems().size()).isEqualTo(1);
		// 주문한 상품의 가격 총합이 정확해야 한다.
		Assertions.assertThat(findOrder.getTotalPrice()).isEqualTo(20000);
		// 주문 수량만큼 재고에서 차감이 되어야 한다.
		Assertions.assertThat(book.getStockQuantity()).isEqualTo(48);
	}

	@Test
	@DisplayName("상품 주문시 현재 재고 수량보다 많은 수량을 주문하면 예외가 발생해야 한다.")
	void createOrderFail() throws NotEnoughStockException {
		// given
		Member member = createMember();
		Book book = createBook("책1", 50000, 5);

		int orderCount = 6;

		// when & then (?)
		assertThrows(NotEnoughStockException.class, () -> orderService.order(member.getId(), book.getId(), orderCount));
	}

	@Test
	@DisplayName("주문 취소")
	void cancleOrder() throws IllegalStateException {
		// given
		Member member = createMember();
		Book book = createBook("책1", 10000, 5);

		Long id = orderService.order(member.getId(), book.getId(), 2);

		Order order = orderRepository.findOne(id);

		// when
		order.cancelOrder();

		// then
		// 주문 취소 시 주문 상태는 ORDER → CANCEL로 변경되어야 한다.
		Assertions.assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCEL);
		// 주문 취소 시 재고가 다시 원상태로 복구가 되어야 한다.
		Assertions.assertThat(book.getStockQuantity()).isEqualTo(5);
	}

	private Book createBook(String name, int price, int stockQuantity) {
		Book book = new Book();
		book.setName(name);
		book.setPrice(price);
		book.setStockQuantity(stockQuantity);
		em.persist(book);
		return book;
	}

	private Member createMember() {
		Member member = new Member();
		member.setName("Kim");
		member.setAddress(new Address("서울", "중랑구", "123-123"));
		em.persist(member);
		return member;
	}
}