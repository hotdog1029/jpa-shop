package jpabook.jpashop.domain.item;


import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 상속관계 매핑 전략 default는 싱글 테이블임(한테이블에 다떄려박는 싱글테이블 전략)
@DiscriminatorColumn(name = "dtype") // 부모 클래스에 선언한다. 하위 클래스를 구분하는 용도의 컬럼이다.
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")

    private Long id;
    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<Category>();
    //==비즈니스 로직==//
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }



}
