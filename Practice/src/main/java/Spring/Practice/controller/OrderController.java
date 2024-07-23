package Spring.Practice.controller;

import Spring.Practice.domain.Member;
import Spring.Practice.domain.Order;
import Spring.Practice.domain.item.Item;
import Spring.Practice.service.ItemService;
import Spring.Practice.service.MemberService;
import Spring.Practice.service.OrderService;
import Spring.Practice.util.OrderSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class OrderController {

	private final OrderService orderService;
	private final MemberService memberService;
	private final ItemService itemService;

	@Autowired
	public OrderController(OrderService orderService, MemberService memberService, ItemService itemService) {
		this.orderService = orderService;
		this.memberService = memberService;
		this.itemService = itemService;
	}

	// 주문 생성 폼
	@GetMapping("/order")
	public String createOrderForm(Model model) {
		List<Item> items = itemService.findItems();
		List<Member> members = memberService.findMembers();

		model.addAttribute("members", members);
		model.addAttribute("items", items);
		return "order/orderForm";
	}

	// 주문 생성
	@PostMapping("/order")
	public String createOrder(@RequestParam(name = "memberId") Long memberId,
							  @RequestParam(name = "itemId") Long itemId,
							  @RequestParam(name = "count") int count) {
		orderService.order(memberId, itemId, count);
		return "redirect:/orders";
	}

	// 주문 목록 검색
	@GetMapping("/orders")
	public String orderList(@ModelAttribute(name = "orderSearch") OrderSearch orderSearch, Model model) {
		List<Order> orders = orderService.findOrders(orderSearch);
		model.addAttribute("orders", orders);
		return "order/orderList";
	}
	
	// 주문 목록 취소
	@PostMapping("/orders/{orderId}/cancel")
	public String cancelOrder(@PathVariable(name = "orderId") Long id) {
		orderService.cancelOrder(id);
		return "redirect:/orders";
	}
}
