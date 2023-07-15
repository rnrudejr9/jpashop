package jpabook.jpashop.repository.order.query;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 화면이나 api 의존 관계에서 떼어낼 려고 하는 것
 * 엔티티가 아닌 것들에 대한 조회문
 *
 *
 */
@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager em;
    
    public List<OrderQueryDto> findOrderQueryDtos(){
        List<OrderQueryDto> result = findOrders();
        //컬렉션 부분은 직접 채움
        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });

        return result;

    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                        "from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id = :orderId",OrderItemQueryDto.class)
                .setParameter("orderId",orderId)
                .getResultList();
    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id,m.name,o.orderDate,o.status, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderQueryDto.class
        ).getResultList();
    }
}
