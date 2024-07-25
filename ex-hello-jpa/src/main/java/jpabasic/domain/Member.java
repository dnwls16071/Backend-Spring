package jpabasic.domain;

import jakarta.persistence.*;
import jpabasic.domain.baseentity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "MEMBERS")
public class Member extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)	// 지연 로딩 설정
	@JoinColumn(name = "team_id")
	private Team team;

	@Embedded
	private Period period;

	/*
	// 회원 주소
	@Embedded
	private Address homeAddress;

	// 회사 주소
	@Embedded
	private Address companyAddress;
	 */

	// Caused by: org.hibernate.MappingException: Column 'city' is duplicated in mapping for entity 'jpabasic.domain.Member' (use '@Column(insertable=false, updatable=false)' when mapping multiple properties to the same column)
	// 매핑 오류 발생 → 임베디드 타입 재정의 필요 → @AttributeOverrides와 @AttributeOverride 사용
	// 임베디드 타입이 null이면 매핑 컬럼 값 모두 null이다.
	@Embedded
	private Address homeAddress;

	@Embedded
	@AttributeOverrides(
			value = {
					@AttributeOverride(name = "city", column = @Column(name = "company_city")),
					@AttributeOverride(name = "street", column = @Column(name = "company_street")),
					@AttributeOverride(name = "zipcode", column = @Column(name = "company_zipcode"))
			})
	private Address companyAddress;
}
