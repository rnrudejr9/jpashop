package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

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



        String jpql = "select o from Order o join o.member m";
        boolean isFirstCondition = true;

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }

        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = entityManager.createQuery(jpql, Order.class)
                .setMaxResults(1000);

        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }

        return query.getResultList();
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

    public List<Order> findAllWithMemberDelivery() {
        return entityManager.createQuery("" +
                "select o from Order o" +
                " join fetch o.member m" +
                " join fetch o.delivery d", Order.class).getResultList();
    }




}
