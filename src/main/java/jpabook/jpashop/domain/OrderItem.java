package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.items.Item;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderItem {
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "order_id")
    private Order order;
    private int orderPrice; //주문가격
    private int count; //주문수량

    /**
     * 생성메서드
     */
    public static OrderItem createOrderItem(Item item,int orderPrice,int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    /**
     * 주문취소
     */
    public void cancel(){
        getItem().addStock(count);
    }


    /**
     * 전체가격조회
     */
    public int getOrderTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
