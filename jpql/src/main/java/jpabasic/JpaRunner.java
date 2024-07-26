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

			// 상태 필드 연관 경로 탐색 - 명시적 조인(외부 조인)
			// 검색하고자 하는 것 : Member
			// 기준 : Member
			// 조인 대상 : Team
			List<Member> list = em.createQuery("select m from Member m join m.team t", Member.class)
					.getResultList();
			for (Member member : list) {
				String name = member.getUsername();
				Integer age = member.getAge();
				System.out.println(name + " : " + age);
			}

			// 상태 필드 연관 경로 탐색 - 묵시적 조인(내부 조인)
			// 검색하고자 하는 것 : Team
			// 기준 : Member
			// 조인 대상 : Team
			List<Team> list1 = em.createQuery("select m.team from Member m", Team.class)
					.getResultList();
			for (Team t : list1) {
				System.out.println(t.getName());
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
