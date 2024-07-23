package Spring.Practice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

	@Embedded
	private Address address;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<Order> orders = new ArrayList<>();
}
