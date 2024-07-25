package jpabasic;

import jakarta.persistence.*;
import jpabasic.domain.Member;
import jpabasic.domain.Order;
import jpabasic.dto.AgeDTO;
import jpabasic.dto.MemberDTO;

import java.util.List;

public class JpaRunner {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();

		try {
			Member m1 = new Member();
			m1.setUsername("m1");
			m1.setAge(10);

			Member m2 = new Member();
			m2.setUsername("m2");
			m2.setAge(15);

			Member m3 = new Member();
			m3.setUsername("m3");
			m3.setAge(20);
			
			// distinct 테스트 위해 Member 객체 추가
			Member m4 = new Member();
			m4.setUsername("m4");
			m4.setAge(20);
			
			em.persist(m1);
			em.persist(m2);
			em.persist(m3);
			em.persist(m4);

			em.flush();
			em.clear();

			// 반환할 타입을 명확히 지정할 수 있으면? → TypedQuery
			TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
			List<Member> result1 = query1.getResultList();
			result1.stream().forEach(m -> System.out.println(m.getUsername()));

			em.flush();
			em.clear();

			// 반환할 타입을 명확히 지정할 수 없다면? → Query
			Query query2 = em.createQuery("select m from Member m");
			List result2 = query2.getResultList();
			result2.stream().forEach(o -> System.out.println(((Member) o).getUsername()));

			em.flush();
			em.clear();

			// 이름 파라미터 바인딩 적용
			Member singleResult = em.createQuery("select m from Member m where m.username = :username", Member.class)
					.setParameter("username", "m2")
					.getSingleResult();
			System.out.println(singleResult.getUsername());

			em.flush();
			em.clear();

			// 엔티티 프로젝션(Ex. Member, Team)
			// 엔티티 프로젝션의 경우 영속성 컨텍스트에서 관리된다.
			List<Member> p1 = em.createQuery("select m.team from Member m", Member.class)
					.getResultList();

			// 임베디드 타입 프로젝션(Ex. Address)
			// 임베디드 타입 프로젝션의 경우 영속성 컨텍스트에서 관리되지 않는다.
			List<Order> p2 = em.createQuery("select o.address from Order o", Order.class)
					.getResultList();

			// 스칼라 타입 프로젝션(Ex. username, age)
			List<Member> p3 = em.createQuery("select m.username from Member m", Member.class)
					.getResultList();

			// 여러 프로젝션 → Object[]
			List<Object[]> p4 = em.createQuery("select m.username, m.age from Member m")
					.getResultList();
			for (Object[] objects : p4) {
				String username = (String) objects[0];
				Integer age = (Integer) objects[1];
				System.out.println(username + " : " + age);
			}

			// 실제 애플리케이션 개발 시 DTO를 만들어 DTO 타입으로 변환
			List<MemberDTO> p5 = em.createQuery("select new jpabasic.dto.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
					.getResultList();
			for (MemberDTO memberDTO : p5) {
				String username = memberDTO.getUsername();
				int age = memberDTO.getAge();
				System.out.println(username + " : " + age);
			}

			// 페이징
			// 예상 결과 : 나이 순 내림차순 정렬 m3 → m2 → m1
			// 그 중에서 조회 시작 위치는 m2(setFirstResult(1)) 조회할 데이터 수는 2개(setMaxResults(2))
			List<Member> paging = em.createQuery("select m from Member m order by m.age desc", Member.class)
					.setFirstResult(1)
					.setMaxResults(2)
					.getResultList();
			for (Member member : paging) {
				System.out.println(member.getUsername());
			}

			// 예상 결과 : distinct로 중복을 제거하므로 10, 15, 20, 20 중에서 20이 중복 제거되어 한 번만 나옴
			List<AgeDTO> list = em.createQuery("select distinct new jpabasic.dto.AgeDTO(m.age) from Member m", AgeDTO.class)
					.getResultList();
			for (AgeDTO ageDTO : list) {
				System.out.println(ageDTO.getAge());
			}
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}
}
