package Spring.Practice.repository;

import Spring.Practice.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class MemberRepository {

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public Long save(Member member) {
		em.persist(member);
		return member.getId();
	}

	public Member findOne(Long id) {
		return em.find(Member.class, id);
	}

	public List<Member> findAll() {
		return em.createQuery("select m from Member m", Member.class)
				.getResultList();
	}

	public List<Member> findByName(String param) {
		return em.createQuery("select m from Member m where m.name = :param", Member.class)
				.setParameter("param", param)
				.getResultList();
	}
}
