package jpabasic.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.time.LocalDate;

@Embeddable
@Getter
public class Period {
	private LocalDate startDate;
	private LocalDate endDate;
}
