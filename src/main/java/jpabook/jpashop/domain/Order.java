package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
public class Order {
    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    // foriegn key 이름이 이렇게 됨
    // 연관관계의 주인은 누구냐? -> jpa는 둘중에 어디서 update를 쳐야되냐
    // 둘중에 하나를 주인으로 잡고 변경하면 됨 (Order가 주인)
    private Member member;

    @OneToMany(mappedBy = "order")
    // OrderItem 안의 order
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne
    @JoinColumn(name="delivery_id")
    //연관관계를 1:1 이면 어디든 둬도 상관없다. 엑세스 많이 하는곳에 두는걸 선호
    private Delivery delivery;
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

}
