package jpabasic.domain.baseentity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
