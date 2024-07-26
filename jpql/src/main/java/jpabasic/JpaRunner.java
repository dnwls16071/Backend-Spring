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

			em.flush();
			em.clear();

			// 컬렉션 필드 연관 경로 표현 - 묵시적 조인
			List<Member> list = em.createQuery("select t.members from Team t", Member.class)
					.getResultList();
			for (Member member : list) {
				System.out.println(member);
			}

			// 컬렉션 필드 연관 경로 표현 - 명시적 조인
			List<Member> list1 = em.createQuery("select m from Team t join t.members m", Member.class)
					.getResultList();
			for (Member member : list1) {
				System.out.println(member);
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
