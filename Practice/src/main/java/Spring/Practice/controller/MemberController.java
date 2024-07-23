package Spring.Practice.controller;

import Spring.Practice.domain.Address;
import Spring.Practice.domain.Member;
import Spring.Practice.service.MemberService;
import Spring.Practice.util.MemberForm;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@Slf4j
public class MemberController {

	private final MemberService memberService;
	@Autowired
	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	// 회원 가입 GET
	@GetMapping("/members/new")
	public String createForm(Model model) {
		log.info("createForm Controller(GET)");
		model.addAttribute("memberForm", new MemberForm());
		return "members/createMemberForm";
	}

	// 회원 가입 Post
	@PostMapping("/members/new")
	public String create(@Valid MemberForm memberForm, BindingResult bindingResult) {
		log.info("create Controller(POST)");
		// 바인딩 오류 발생 시 → 회원 가입 페이지로 다시 보내기
		if (bindingResult.hasErrors()) {
			return "members/createMemberForm";
		}
		Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());
		Member member = new Member();
		member.setName(memberForm.getName());
		member.setAddress(address);
		memberService.join(member);
		return "redirect:/";
	}

	// 회원 목록 조회 GET
	@GetMapping("/members")
	public String list(Model model) {
		log.info("list Controller(GET)");
		List<Member> members = memberService.findMembers();
		model.addAttribute("members", members);
		return "members/memberList";
	}
}
