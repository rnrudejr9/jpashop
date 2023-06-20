package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    // foriegn key 이름이 이렇게 됨
    // 연관관계의 주인은 누구냐? -> jpa는 둘중에 어디서 update를 쳐야되냐
    // 둘중에 하나를 주인으로 잡고 변경하면 됨 (Order가 주인)
    private Member member;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    // OrderItem 안의 order
    // orderItemA,B,C 를 persist 해주고 persist(order)해야되는데, cascade 하면 order만 해주면 됨
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="delivery_id")
    //연관관계를 1:1 이면 어디든 둬도 상관없다. 엑세스 많이 하는곳에 두는걸 선호
    private Delivery delivery;
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;



    //연관관계 메서드
//    member에서 설정과 order에서 설정을 해줘야되는 것인데 까먹을 수 있다.
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }
    public void addOrderItem(OrderItem orderItem){
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    /**
     * 생성 메서드
     * 응집해둔것이라 주문과 관계된 비즈니스는 여기서 보면됨
     */
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    /**
     * 비즈니스 로직
     * 주문취소
     */
    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송된 상품입니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        for(OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }

    /**
     * 조회로직
     * ctrl + art + n : 변수 정리
     */
    public int getTotalPrice(){
        return orderItems.stream()
                .mapToInt(OrderItem::getOrderTotalPrice)
                .sum();
    }

}
