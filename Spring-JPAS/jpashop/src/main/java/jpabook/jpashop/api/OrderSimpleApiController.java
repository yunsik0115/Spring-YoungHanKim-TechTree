package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;


/**
 * Order 조회 ( TO ONE RELATION OPTIMIZATION )
 * Order -> Member X TO ONE
 * Order -> Delivery X TO ONE
 * Order -> OrderItems X TO MANY (COLLECTION)
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        // Entity 직접 노출시 문제 (양방향 중 하나는 @JsonIgnore 해야함)
        List<Order> all = orderRepository.findAll(new OrderSearch()); // 검색 조건이 없어서 다 가져옴
        // DB에서 갖고 올때는 LAZY 객체를 가져오지 않음 근데 NULL을 넣어둘수는 없어서 new Member() 해서 Proxy 객체를 가져오는데
        // PROXY -> ByteBuddy
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            // lazy loading 강제 초기화 -> 이 방법을 사용하는 경우 Hibernate 강제 LazyLoading 필요 없음
        }
        return all;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2(){ // Be aware that return type should be wrapped to another class
        List<Order> orders = orderRepository.findAll(new OrderSearch());
        // Order 2개라 가정

        // N + 1 -> 1 + 회원 N + 배송 N (쿼리 날아감)
        // Eager로 바꿔도 해결이 안됨....

        List<SimpleOrderDto> result = orders.stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList()); // 루프 돌릴때 무슨 일이 일어나냐 -> getname해서 가져올때 Lazy가 초기화됨(영속성 컨텍스트가 찾아봄 -> 없다, 쿼리 날림)

        // 처음 주문서의 order 2개 찾아야 함 member 쿼리
        // SimpleOrderDto

        // JPA Lazy Loading 매커니즘
        // -> 맨 처음에 Order 조회 -> SQL 한번 실행하면 2개 나옴
        // 한번 실행해서 결과 row수가 2개가 나온 것임
        // 2개면 stream() 루프 두번 돈다
        // 1회 돌때 처음에 select 쿼리로 가져올때 2개
        return result;
    }

    // V1과 V2의 공통문제 -> 쿼리가 너무 많이 나감 -> Order Delivery Member 3개

    @Data
    static class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }
}
