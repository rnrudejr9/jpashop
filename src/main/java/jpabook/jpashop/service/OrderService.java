package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.items.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    /**
     * 주문
     * 생성로직을 넣을때, new 연산을 막기위해 protected 접근 제한자로 넣으면 도미
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count){
        //엔티티조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장 cascade 되었기 때문에 연쇄적으로 저장됨
        //라이프사이클에서 동일하게 관리될때 연쇄적으로 사용하면 됨
        //다른곳에서 딜리버리 엔티티를 참조할때 함부로 지우면 안된다.
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 취소
     */
    @Transactional
    public void cancel(Long orderId){
        Order order = orderRepository.findOne(orderId);
        order.cancel();
        // JPA 강점이 나옴
        // ++ 수정한 엔티티에 대한 update 쿼리를 날려야하는데, JPA는 엔티티 더티체킹(변경감지) 내역을 찾아 쿼리가 날라감!!
        // 엔티티에 핵심로직을 넣어 처리하는 패턴 Domain Model Pattern 이라고 함 -> JPA, ORM 사용시 이렇게 짜면 좋음
        // 서비스계층에서 핵심로직을 넣어 처리하는 패턴 Transaction script Pattern 이라고 함
        // 사실 뭐가 더 유지보수가 좋을지 판단해서 사용하면 됨

    }

    /**
     * 검색
     */
//    public List<Order> findOrders(OrderSearch orderSearch){
//        return orderRepository.findAll();
//    }
}
