package Spring.Practice.util;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberForm {

	@NotEmpty(message = "회원명은 빈 값이 올 수 없습니다!")
	private String name;
	private String city;
	private String street;
	private String zipcode;
}
