package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.items.Book;
import jpabook.jpashop.domain.items.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired EntityManager em;
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderService orderService;

    private Member createMember(){
        Member member = new Member();
        member.setName("Spring");
        member.setAddress(new Address("seoul","s","s"));
        em.persist(member);
        return member;
    }
    private Book createBook(){
        Book book = new Book();
        book.setName("JPA Hibernate");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);
        return book;
    }

    @Test
    void 상품_주문() {
        Member member = createMember();
        Book book = createBook();

        //when
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order order = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER,order.getStatus(),"상품 주문시 상태 ORder");
        assertEquals(1,order.getOrderItems().size(),"주문한 상품의 종류의 수가 정확해야한다.");
        assertEquals(10000*orderCount, order.getTotalPrice(),"총 가격 수량 같다");
        assertEquals(8, book.getStockQuantity(),"총 가격 수량 같다");
    }

    @Test
    void 상품_주문_취소() {
        Member member = createMember();
        Book book = createBook();

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        orderService.cancel(orderId);

        Order order = orderRepository.findOne(orderId);

        assertEquals(order.getStatus(),OrderStatus.CANCEL);
        assertEquals(10,book.getStockQuantity());
    }

    @Test
    void 재고수량_초과() throws Exception{
        Member member = createMember();
        Book book = createBook();

        int orderCount = 11;

        assertThrows(NotEnoughStockException.class,()->{
            orderService.order(member.getId(),book.getId(),orderCount);
        });

    }
}