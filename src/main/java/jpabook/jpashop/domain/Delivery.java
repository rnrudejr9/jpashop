package jpabook.jpashop.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name="delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    //거울이다.
    private Order order;

    @Embedded
    private Address address;

    //Ordinal 숫자로 들어감 꼭 STRING
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; //Ready ,COMP
}
