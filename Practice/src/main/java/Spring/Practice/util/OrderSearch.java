package Spring.Practice.util;

import Spring.Practice.domain.enumType.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearch {
	private String memberName;			// 회원명
	private OrderStatus orderStatus;	//주문 상태
}
