package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
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
     * Order
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count){

        // Entity Retrieving
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // Creating Shipment Info
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // Creating OrderItem Info
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        // orderItem.save()??? -> CASCADE

        // Creating Order Info (Giving orderItem as a parameter)
        Order order = Order.createOrder(member, delivery, orderItem);
        /* 3 Creation procedure requires persist but only orderRepository.save() was executed why?
        * cascade = CascadeType.All in Delivery, OrderItems
        * Boundary of CASCADING? -> Ambiguous but When has Single Reference(private owner) and if has same life cycle
        * else do not use*/

        // Saving Order Info
        orderRepository.save(order);
        return order.getId();

    }

    // Order Cancellation
    @Transactional
    public void cancelOrder(Long orderId){
        Order order = orderRepository.findOne(orderId);
        order.cancel();
        // Update Query??? -> Transactional Script Requires query language on Service Layer but JPA doesn't
    }

     //Order Search
    public List<Order> findOrders(OrderSearch ordersearch){
        return orderRepository.findAll(ordersearch);
    }


}
