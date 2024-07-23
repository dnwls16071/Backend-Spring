package Spring.Practice.service;

import Spring.Practice.domain.Member;
import Spring.Practice.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

	@Autowired MemberService memberService;
	@Autowired MemberRepository memberRepository;

	@Test
	@DisplayName("회원가입 테스트")
	void join() throws Exception {
		// given
		Member member = new Member();
		member.setName("Kim");

		// when
		Long id = memberService.join(member);

		// then
		Assertions.assertThat(member).isEqualTo(memberRepository.findOne(id));
	}

	@Test
	@DisplayName("중복 회원 검증")
	void isDuplicateMember() throws Exception {
		// given
		Member m1 = new Member();
		m1.setName("Kim");

		Member m2 = new Member();
		m2.setName("Kim");

		// when
		memberService.join(m1);

		// then
		assertThrows(IllegalStateException.class, () -> memberService.join(m2));
	}
}