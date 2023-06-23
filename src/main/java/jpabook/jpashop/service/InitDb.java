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
        initService.dbInit2();
    }


    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{
        private final EntityManager em;

        public void dbInit(){
            Member member =createMember("userA","서울","1","1111");
            em.persist(member);

            Book book = createBook("JPA1", 10090, 100);
            em.persist(book);
            Book book2 = createBook("JPA2", 20000, 200);;
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book, 10000, 1);
            OrderItem ordetItem2 = OrderItem.createOrderItem(book2,20000,2);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, ordetItem2);
            em.persist(order);
        }

        private static Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }

        private Book createBook(String name, int price, int stockQuantity){
            Book book = new Book();
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(stockQuantity);
            return book;
        }

        private Member createMember(String name,String city,String street,String zipcode){
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city,street,zipcode));
            return member;
        }


        public void dbInit2(){
            Member member = createMember("userB","진주","2","2222");
            em.persist(member);

            Book book = createBook("SQL1",1000,10000);
            em.persist(book);

            Book book2 = createBook("SQL2",1000,100000);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book, 10000, 1);
            OrderItem ordetItem2 = OrderItem.createOrderItem(book2,20000,2);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, ordetItem2);
            em.persist(order);
        }
    }
}

