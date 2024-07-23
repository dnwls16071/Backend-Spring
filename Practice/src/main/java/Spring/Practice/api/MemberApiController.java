package Spring.Practice.api;

import Spring.Practice.domain.Member;
import Spring.Practice.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MemberApiController {

	private final MemberService memberService;
	@Autowired
	public MemberApiController(MemberService memberService) {
		this.memberService = memberService;
	}

	@PostMapping("/api/v1/members")
	public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
		Long id = memberService.join(member);
		return new CreateMemberResponse(id);
	}

	@PostMapping("/api/v2/members")
	public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
		Member member = new Member();
		member.setId(request.getId());
		member.setName(request.getName());
		Long id = memberService.join(member);
		return new CreateMemberResponse(id);
	}

	@PutMapping("/api/v2/members/{id}")
	public UpdateMemberResponse updateMemberV2(@PathVariable(name = "id") Long id, @RequestBody @Valid UpdateMemberRequest request) {
		// 변경의 경우 가급적 반환값이 없게끔 설계하는 것이 좋다.
		memberService.update(id, request.getName());
		Member member = memberService.findOne(id);
		return new UpdateMemberResponse(member.getId(), member.getName());
	}

	@GetMapping("/api/v1/members")
	public List<Member> membersV1() {
		return memberService.findMembers();
	}

	// Result 제네릭 클래스로 컬렉션을 감싸면 향후 필요한 필드 추가도 쉽게 할 수 있다.
	// 컬렉션을 직접 반환하게 되면 향후 API 스펙을 변경하기가 매우 까다로워진다.
	// 별도의 Result 클래스 생성으로 해결이 가능하다.ㄷ
	@GetMapping("/api/v2/members")
	public Result membersV2() {
		List<Member> members = memberService.findMembers();
		List<MemberDTO> collect = members.stream()
				.map(member -> new MemberDTO(member.getName()))
				.collect(Collectors.toList());
		return new Result(collect);
	}

	// 제네릭 도입
	@Data
	static class Result<T> {
		private T data;

		public Result(T data) {
			this.data = data;
		}
	}

	@Data
	static class MemberDTO {
		private String name;

		public MemberDTO(String name) {
			this.name = name;
		}
	}

	@Data
	static class CreateMemberResponse {
		private Long id;

		public CreateMemberResponse(Long id) {
			this.id = id;
		}
	}

	@Data
	static class CreateMemberRequest {
		private Long id;

		@NotEmpty
		private String name;
	}

	@Data
	static class UpdateMemberResponse {
		private Long id;
		private String name;

		public UpdateMemberResponse(Long id, String name) {
			this.id = id;
			this.name = name;
		}
	}

	@Data
	static class UpdateMemberRequest {
		private String name;
	}
}
