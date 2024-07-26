package jpabasic;

import jakarta.persistence.*;
import jpabasic.domain.Member;
import jpabasic.domain.Team;

import java.util.List;

public class JpaRunner {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();

		try {
			// 주어진 케이스
			Team team = new Team();
			team.setName("team");
			em.persist(team);

			Member m1 = new Member();
			m1.setUsername("member1");
			m1.setAge(15);
			m1.setTeam(team);
			em.persist(m1);

			Member m2 = new Member();
			m2.setUsername("member2");
			m2.setAge(16);
			m2.setTeam(team);
			em.persist(m2);

			Member m3 = new Member();
			m3.setAge(17);
			m3.setUsername("member3");
			m3.setTeam(team);
			em.persist(m3);

			// 단일 값 경로 탐색 (1)
			List<Integer> list = em.createQuery("select m.age from Member m", Integer.class)
					.getResultList();
			for (Integer integer : list) {
				System.out.println(integer);
			}

			// 단일 값 경로 탐색 (2)
			List<String> list1 = em.createQuery("select m.username from Member m", String.class)
					.getResultList();
			for (String string : list1) {
				System.out.println(string);
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
