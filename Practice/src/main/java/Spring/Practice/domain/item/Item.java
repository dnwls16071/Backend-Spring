package Spring.Practice.domain.item;

import Spring.Practice.domain.Category;
import Spring.Practice.exception.NotEnoughStockException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_id")
	private Long id;
	private String name;
	private int price;
	private int stockQuantity;

	@ManyToMany(mappedBy = "items")
	private List<Category> categories = new ArrayList<>();

	// 상품 엔티티에 비즈니스 로직 추가(재고 증가)
	public void addStock(int quantity) {
		this.stockQuantity += quantity;
	}

	// 상품 엔티티에 비즈니스 로직 추가(재고 감소)
	public void removeStock(int quantity) {
		int restStock = stockQuantity - quantity;
		if (restStock < 0) {
			throw new NotEnoughStockException("주문 수량이 재고 수량보다 많아 주문이 불가합니다.");
		}
		stockQuantity = restStock;
	}
}
