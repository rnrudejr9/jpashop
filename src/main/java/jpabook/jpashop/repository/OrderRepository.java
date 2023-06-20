package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager entityManager;

    public void save(Order order){
        entityManager.persist(order);
    }

    public Order findOne(Long id){
        return entityManager.find(Order.class,id);
    }

    public List<Order> findAll(OrderSearch orderSearch){
        //이름이 있을때없을떄, 상태가 있을때없을때 어떻게 동적쿼리를 만들것이냐


        return entityManager.createQuery("select o from Order o join o.member m" +
                        " where o.status = :status and m.name like :name"
                , Order.class)
                .setParameter("status",orderSearch.getOrderStatus())
                .setParameter("name",orderSearch.getMemberName())
                .setFirstResult(0)
                .setMaxResults(1000)
                .getResultList();
    }

    /**
     * JPA Criteria
     *
     * @param orderSearch
     * @return
     */
    public List<Order> findAllByCriteria(OrderSearch orderSearch){
        //이름이 있을때없을떄, 상태가 있을때없을때 어떻게 동적쿼리를 만들것이냐


        return entityManager.createQuery("select o from Order o join o.member m" +
                                " where o.status = :status and m.name like :name"
                        , Order.class)
                .setParameter("status",orderSearch.getOrderStatus())
                .setParameter("name",orderSearch.getMemberName())
                .setFirstResult(0)
                .setMaxResults(1000)
                .getResultList();
    }
}
