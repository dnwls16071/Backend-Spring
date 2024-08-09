package study.springdatajpa.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.springdatajpa.domain.Member;
import study.springdatajpa.repository.MemberRepository;

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

	@PostConstruct
	public void init() {
		memberRepository.save(new Member("member"));
	}
}
