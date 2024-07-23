package Spring.Practice.util;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemForm {
	private Long id;
	@NotEmpty(message = "빈 값이 들어올 수 없습니다!")
	private String name;

	@NotNull
	@Min(0)
	private Integer price;

	@NotNull
	@Min(0)
	private Integer stockQuantity;

	@NotEmpty(message = "빈 값이 들어올 수 없습니다!")
	private String author;

	@NotEmpty(message = "빈 값이 들어올 수 없습니다!")
	private String isbn;
}
