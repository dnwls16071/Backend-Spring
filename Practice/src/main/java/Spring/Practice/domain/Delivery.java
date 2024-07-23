package Spring.Practice.domain;

import Spring.Practice.domain.enumType.DeliveryStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Delivery {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "delivery_id")
	private Long id;

	@Enumerated(EnumType.STRING)
	private DeliveryStatus deliveryStatus;

	@Embedded
	private Address address;

	@OneToOne(mappedBy = "delivery")
	private Order order;
}
