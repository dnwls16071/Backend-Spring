package jpabasic.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "MEMBERS")
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)	// 지연 로딩 설정
	@JoinColumn(name = "team_id")
	private Team team;
}
