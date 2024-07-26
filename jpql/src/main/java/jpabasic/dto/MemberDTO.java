package jpabasic.dto;

import lombok.Getter;

@Getter
public class MemberDTO {
	private String username;
	private int age;

	public MemberDTO(String username, int age) {
		this.username = username;
		this.age = age;
	}
}
