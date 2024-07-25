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
			Team team1 = new Team();
			team1.setName("팀A");
			em.persist(team1);

			Team team2 = new Team();
			team2.setName("팀B");
			em.persist(team2);

			Member m1 = new Member();
			Member m2 = new Member();
			Member m3 = new Member();
			Member m4 = new Member();
			Member m5 = new Member();

			m1.setTeam(team1);
			m2.setTeam(team1);
			m3.setTeam(team2);
			m4.setTeam(team2);

			em.persist(m1);
			em.persist(m2);
			em.persist(m3);
			em.persist(m4);
			em.persist(m5);

			em.flush();
			em.clear();

			// 엔티티 내부 조인
			System.out.println("엔티티 내부 조인");
			List<Member> join1 = em.createQuery("select m from Member m inner join m.team t", Member.class)
					.getResultList();
			for (Member member : join1) {
				System.out.println(member);
			}

			em.flush();
			em.clear();
			
			// 엔티티 외부 조인
			System.out.println("엔티티 외부 조인");
			List<Member> join2 = em.createQuery("select m from Member m left join m.team t", Member.class)
					.getResultList();
			for (Member member : join2) {
				System.out.println(member);
			}
			
			em.flush();
			em.clear();
			
			// 컬렉션 조인
			// 현재 팀A에 소속된 멤버는 총 4명
			// 일대다 컬렉션 조인의 경우 다(N)에 해당하는 만큼의 데이터 뻥튀기가 발생하는 문제가 발생
			System.out.println("일대다 컬렉션 조인");
			List<Team> join3 = em.createQuery("select t from Team t join t.members", Team.class)
					.getResultList();
			for (Team t : join3) {
				System.out.println(t.getName() + " : " + t.getMembers().size());
			}
			
			em.flush();
			em.clear();
			
			// 컬렉션 조인
			// 현재 팀A에 소속된 멤버는 총 4명
			// 위에서 제기된 일대다 컬렉션의 문제점을 해결하기 위해 애플리케이션 차원에서 중복을 제거하는 distinct 키워드를 지원
			System.out.println("일대다 컬렉션 조인 - distinct 키워드");
			List<Team> join4 = em.createQuery("select distinct t from Team t join t.members", Team.class)
					.getResultList();
			for (Team t : join4) {
				System.out.println(t.getName() + " : " + t.getMembers().size());
			}

			em.flush();
			em.clear();
			
			// 엔티티 페치 조인
			// 팀과 멤버를 함께 조인하여 한 번에 가져옴
			System.out.println("엔티티 페치 조인");
			List<Member> join5 = em.createQuery("select m from Member m join fetch m.team t", Member.class)
					.getResultList();
			for (Member member : join5) {
				System.out.println(member);
			}

			// 컬렉션 페치 조인 + distinct
			System.out.println("컬렉션 페치 조인");
			List<Team> join6 = em.createQuery("select distinct t from Team t join fetch t.members", Team.class)
					.getResultList();
			for (Team t : join6) {
				System.out.println(t.getName());
				for (Member member : t.getMembers()) {
					System.out.println(member);
				}
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
