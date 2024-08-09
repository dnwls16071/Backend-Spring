package study.springdatajpa.repository.custom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import study.springdatajpa.domain.Member;

import java.util.List;

public class MemberRepositoryImpl implements MemberRepositoryCustom{

	// 순수 JPA로 직접 구현하고싶을때
	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Member> findMemberCusrom() {
		return em.createQuery("select m from Member m", Member.class)
				.getResultList();
	}
}
