package jpabasic;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jpabasic.domain.*;

import java.util.List;

public class JpaRunner {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();

		try {
			Item item = new Item();
			item.setName("잡동사니");
			em.persist(item);

			Member member = new Member();
			member.setName("고객");
			em.persist(member);

			// 잡동사니 상품 주문 추가
			OrderItem orderItem = new OrderItem();
			orderItem.setItem(item);
			em.persist(orderItem);

			// 주문 생성
			Order order = new Order();
			order.setMember(member);
			order.addOrderItem(orderItem);	// 연관관계 편의 메서드 사용
			em.persist(order);

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}
}
