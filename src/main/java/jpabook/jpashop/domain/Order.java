package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 자기 기준으로 member 엔티티와 다대일 관계이기 때문에
    @JoinColumn(name = "member_id") // FK를 member_id로 쓸것이다.
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // Order Item 안에있는 컬랙션을 한꺼번에 persist 할 수 있고 delete 할 수 있음
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // 원래는 persist를 엔티티 각각 해줘야하는데 cascade설정으로 Order에 persist할 때 Delivery에 자동으로 persist됨
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING) //ordinal 쓰지말고 꼭 string으로 쓰자
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    //==연관관계 메서드==// 관계가 양방향일 떄 셋팅하는거임
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 매서드==//
    // 생성지점을 변경할려면 여기만 변경하면 되기 때문에 이러한 코딩 스타일이 좋다.
    public static Order crateOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER); // 디폴트값은 ORDER로 유지
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직==//
    /**
     * 주문 취소
     */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //==조회 로직==//

    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalprice();
        }
        return totalPrice;
    }





}
