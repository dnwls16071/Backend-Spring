package study.springdatajpa.repository.custom;

import study.springdatajpa.domain.Member;

import java.util.List;

public interface MemberRepositoryCustom {
	List<Member> findMemberCusrom();
}
