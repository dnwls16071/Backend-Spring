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

			Team newTeam = new Team();
			newTeam.setName("이전할 팀");
			em.persist(newTeam);

			Member member = new Member();
			member.setName("member");
			member.setTeam(team);
			em.persist(member);

			em.flush();	// 영속성 컨텍스트 플러시 → 쓰기 지연 SQL 저장소에 등록된 쿼리를 DB에 반영
			em.clear();	// 영속성 컨텍스트 초기화

			Member findMember = em.find(Member.class, member.getId());// 1차 캐시에 없기에 우선적으로 DB를 조회하여 1차 캐시로 가져오고 영속성 컨텍스트에서 관리되도록
			findMember.setTeam(newTeam);	// 새로운 팀으로 변경(수정)

			findMember.setTeam(null);	// 엔티티 삭제(연관관계를 제거해줘야 에러가 발생하지 않는다.)
			em.persist(findMember);

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}
		emf.close();
	}
}
