package jpabasic.domain;

import jakarta.persistence.*;
import jpabasic.domain.enumType.DeliveryStatus;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "DELIVERY")
public class Delivery {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "delivery_id")
	private Long id;
	private String city;
	private String street;
	private String zipcode;
	private DeliveryStatus status;
}
