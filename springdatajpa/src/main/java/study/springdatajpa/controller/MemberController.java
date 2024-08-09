package study.springdatajpa.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.springdatajpa.domain.Member;
import study.springdatajpa.dto.MemberDto;
import study.springdatajpa.repository.MemberRepository;

import java.util.stream.Collectors;

@RestController
public class MemberController {

	private final MemberRepository memberRepository;

	@Autowired
	public MemberController(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	@GetMapping("/members/{id}")
	public String findMember(@PathVariable(name = "id") Long id) {
		Member member = memberRepository.findById(id).get();
		return member.getUsername();
	}

	// 엔티티를 직접 파라미터에 넣는 방식 - 도메인 클래스 컨버터
	// HTTP 요청은 회원 id를 받는데 도메인 클래스 컨버터가 중간에 동작해서 회원 엔티티 객체를 반환
	// 도메인 클래스 컨버터도 리포지토리를 사용해서 엔티티를 찾음
	@GetMapping("/members2/{id}")
	public String findMember2(@PathVariable(name = "id") Member member) {
		return member.getUsername();
	}

	// 스프링 데이터 JPA에서 제공하는 페이징과 정렬 사용
	// 특정 페이지 데이터 조회 : /members?page=2
	// 정렬 : /members?page=2&sort=id,desc
	// 거듭 강조하지만 엔티티를 직접 외부에 노출시키는 것은 금한다.
	// 기본 사이즈 : 20
	@GetMapping("/members")
	public Page<MemberDto> list(Pageable pageable) {
		Page<Member> page = memberRepository.findAll(pageable);
		return page.map(MemberDto::new);
	}

	@PostConstruct
	public void init() {
		for (int i = 0; i < 100; i++) {
			memberRepository.save(new Member("member" + i, i));
		}
	}
}
