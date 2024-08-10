package study.springdatajpa.repository.custom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.springdatajpa.domain.Member;
import study.springdatajpa.repository.MemberRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryImplTest {

	private final MemberRepository memberRepository;

	@Autowired
	MemberRepositoryImplTest(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	@Test
	@Transactional
	void callCustom() {
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member2", 12));
		memberRepository.save(new Member("member3", 14));
		memberRepository.save(new Member("member4", 16));
		memberRepository.save(new Member("member5", 18));

		List<Member> result = memberRepository.findMemberCusrom();

		Assertions.assertThat(result.size()).isEqualTo(5);
	}
}