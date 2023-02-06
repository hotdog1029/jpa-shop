package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class orderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    orderService orderService;

    @Test
    public void 상품주문() throws Exception{
        //given
        Member member = createMember();

        Book book = createBook("실전 JPA", 10000, 10);

        //when
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertThat(getOrder.getStatus()).isEqualTo(OrderStatus.ORDER); // 오더의 상태가 ORDER이 맞는지 확인
        assertThat(getOrder.getOrderItems().size()).isEqualTo(1); // 상품 등록 종류가 1개가 맞는지 확인
        assertThat(getOrder.getTotalPrice()).isEqualTo(10000 * orderCount); // 총 주문 가격이 맞는지 확인
        assertThat(book.getStockQuantity()).isEqualTo(8); // 남은 재고 수가 맞는지 확인

    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {
        //given
        Member member = createMember();
        Book book = createBook("실전 JPA", 10000, 10);
        int orderCount = 11;
        //when
        orderService.order(member.getId(), book.getId(), orderCount);
        //then
        fail("재고 수량 예외가 발생해야 한다.");

    }

    @Test
    public void 주문취소() throws Exception{
        //given
        Member member = createMember();
        Book book = createBook("실전 JPA", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        //when
        orderService.cancelOrder(orderId);
        //then
        Order order = orderRepository.findOne(orderId);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCEL);

    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }


}