package jpabasic;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
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
			Team team = new Team();
			team.setName("team");
			em.persist(team);

			Member member = new Member();
			member.setName("member");
			member.setTeam(team);
			em.persist(member);

			Team findTeam = member.getTeam();
			System.out.println("팀 이름: " + findTeam.getName());	// 객체 그래프 탐색

			// team에 속한 모든 팀원을 조회하는 JPQL 쿼리문 작성해보기
			List<Member> resultList = em.createQuery("select m from Member as m join m.team as t where t.name = :teamName", Member.class)
					.setParameter("teamName", "team")
					.getResultList();
			resultList.forEach(m -> System.out.println(m.getName()));

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}
}
