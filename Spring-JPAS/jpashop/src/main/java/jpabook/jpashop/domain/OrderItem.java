package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    @JsonIgnore
    private Order order;

    private int orderPrice; // 주문 가격
    private int count; // 주문 수량

//    protected OrderItem(){
//
//    }


    // === Business Logic === //
    public void cancel() {
        getItem().addStockQuantity(count);
    }

    // === Data Retrieving Methods ===//

    public int getTotalPrice(){
        return getOrderPrice() * getCount();
    }

    // === Creation Method === //

    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);
        // item stock quantity should be removed as OrderItem was created. (as much as count)
        item.reduceStockQuantity(count);
        return orderItem;
    }

}
