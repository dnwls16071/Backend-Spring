package jpabasic.dto;

import lombok.Getter;

@Getter
public class AgeDTO {
	private int age;

	public AgeDTO(int age) {
		this.age = age;
	}
}
