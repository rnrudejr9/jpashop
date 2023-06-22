package jpabook.jpashop.service;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.items.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * 총 주문 2개
 */
@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;

    /**
     * 애플리케이션 로딩 시점에 넣어주고 싶어서 사용함
     * 스프링 빈이 이것을 호출해준다.
     * 안에서 트랜잭션 어려워서 별도의 메소드를 통해서 넣어줌
     */
    @PostConstruct
    public void init(){
        initService.dbInit();
    }


    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{
        private final EntityManager em;

        public void dbInit(){
            Member member = new Member();
            member.setName("userA");
            member.setAddress(new Address("서울","1","1111"));
            em.persist(member);

            Book book = new Book();
            book.setName("JPA1");
            book.setPrice(10000);
            book.setStockQuantity(100);
            em.persist(book);

            Book book2 = new Book();
            book2.setName("JPA1");
            book2.setPrice(10000);
            book2.setStockQuantity(100);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book, 10000, 1);
            OrderItem ordetItem2 = OrderItem.createOrderItem(book2,20000,2);


            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderItem1, ordetItem2);
            em.persist(order);
        }

    }
}

