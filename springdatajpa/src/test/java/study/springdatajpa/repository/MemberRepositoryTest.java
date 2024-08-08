package study.springdatajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.springdatajpa.domain.Member;
import study.springdatajpa.domain.Team;
import study.springdatajpa.dto.MemberDto;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class MemberRepositoryTest {

	@Autowired private MemberRepository memberRepository;
	@Autowired private TeamRepository teamRepository;
	@PersistenceContext	private EntityManager em;

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

	@Test
	@Transactional
	void findUsernameList() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		List<Member> usernameList = memberRepository.findByNames(List.of("AAA", "BBB"));
		Assertions.assertThat(usernameList.size()).isEqualTo(2);
	}

	@Test
	@Transactional
	void paging() {
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member2", 10));
		memberRepository.save(new Member("member3", 10));
		memberRepository.save(new Member("member4", 10));
		memberRepository.save(new Member("member5", 10));

		// pageNumber의 값으로 0을 줬는데 Page의 시작은 1이 아니라 0부터이다.
		PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

		// 엔티티를 외부에 직접 노출시키면 메모리 내에서 페이징이 일어나 정말 위험할 수 있게 된다.
		Page<Member> page = memberRepository.findByAge(10, pageRequest);
		List<MemberDto> collect = page.stream()
				.map(member -> new MemberDto(member.getId(), member.getUsername(), null))
				.collect(Collectors.toList());

		/*
		.getContent()       // 조회된 데이터
		.getTotalElements() // 전체 데이터
		.getNumber()        // 페이지 번호
		.getTotalPages()    // 전체 페이지 번호
		.isFirst()			// 첫 번째 항목인가?
		.hasNext()			// 다음 페이지가 존재하는가?
		 */
		List<Member> content = page.getContent();
		Assertions.assertThat(content.size()).isEqualTo(3);
		Assertions.assertThat(page.getTotalElements()).isEqualTo(5); // (Page OK, Slice NO)
		Assertions.assertThat(page.getNumber()).isEqualTo(0);
		Assertions.assertThat(page.getTotalPages()).isEqualTo(2);	 // (Page OK, Slice NO)
		Assertions.assertThat(page.isFirst()).isTrue();
		Assertions.assertThat(page.hasNext()).isTrue();
	}

	@Test
	@Transactional
	void bulkUpdate() {
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member2", 12));
		memberRepository.save(new Member("member3", 14));
		memberRepository.save(new Member("member4", 16));
		memberRepository.save(new Member("member5", 18));

		List<Member> beforeBulk = em.createQuery("select m from Member m", Member.class).getResultList();
		for (Member member : beforeBulk) {
			System.out.println(member.getUsername() + " : " + member.getAge());
		}

		// 벌크성 수정 쿼리문 실행(UPDATE문, JPA의 영속성 컨텍스트에는 반영되지 않고 바로 DB에 반영됨)
		// 데이터 일관성 문제가 발생할 우려가 있으므로 벌크성 수정 쿼리문을 실행했다면 JPA 영속성 컨텍스트를 초기화하는 것이 좋다.
		// @Modifying(clearAutomatically = true) 옵션 사용 권장
		int resultCount = memberRepository.bulkAgePlus(17);
		em.flush();
		em.clear();

		List<Member> afterBulk = em.createQuery("select m from Member m", Member.class).getResultList();
		for (Member member : afterBulk) {
			System.out.println(member.getUsername() + " : " + member.getAge());
		}
	}
}