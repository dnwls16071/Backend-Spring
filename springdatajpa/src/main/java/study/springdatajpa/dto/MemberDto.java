package study.springdatajpa.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.springdatajpa.domain.Member;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
	private Long id;
	private String username;
	private String teamName;

	// 엔티티를 파라미터로 넣어 DTO로 변환
	public MemberDto(Member member) {
		this.id = member.getId();
		this.username = member.getUsername();
		this.teamName = member.getTeam().getName();
	}
}
