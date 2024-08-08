package study.springdatajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.springdatajpa.domain.Member;
import study.springdatajpa.domain.Team;
import study.springdatajpa.dto.MemberDto;

import java.util.List;

@SpringBootTest
class MemberRepositoryTest {

	@Autowired private MemberRepository memberRepository;
	@Autowired private TeamRepository teamRepository;

	@Test
	@Transactional
	void testQuery() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);

		memberRepository.save(m1);
		memberRepository.save(m2);

		List<Member> result = memberRepository.findUser("AAA", 10);
		Assertions.assertThat(result.get(0)).isEqualTo(m1);
	}

	@Test
	@Transactional
	void testDtoQuery() {
		Team team = new Team("teamA");
		teamRepository.save(team);

		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		m1.setTeam(team);
		m2.setTeam(team);
		memberRepository.save(m1);
		memberRepository.save(m2);

		List<MemberDto> dtoList = memberRepository.findUserDto();
		Assertions.assertThat(dtoList.size()).isEqualTo(2);
	}
}