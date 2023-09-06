package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.item.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @PersistenceContext
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception{

        // Given

        Member member =createMember();
        Item item = createBook("시골 JPA", 10000, 10);
        int orderCount = 2;

        // When

        Long orderId = orderService.order(member.getId(), item.getItemId(), orderCount);


        // Then
        Order getOrder = orderRepository.findOne(orderId);

        Assertions.assertEquals( OrderStatus.ORDER, getOrder.getStatus(),"상품 주문시 상태는 ORDER");
        Assertions.assertEquals(1, getOrder.getOrderItems().size(),"주문한 상품 종류 수가 정확해야 한다.");
        Assertions.assertEquals( 10000 * 2, getOrder.getTotalPrice(),"주문 가격은 가격 * 수량이다.");
        Assertions.assertEquals(8, item.getStockQuantity(),"주문 수량만큼 재고가 줄어야 한다.");

    }

    @Test
    public void 상품주문_재고수량초과() throws Exception{
        // Given
        Member member = createMember();
        Item item = createBook("시골 JPA 1", 100, 10);

        int orderCount = 11;

        Assertions.assertThrows(NotEnoughStockException.class, () -> orderService.order(member.getId(), item.getItemId(), orderCount) );
    }

    @Test
    public void 주문취소(){
        Member member = createMember();
        Item item = createBook("시골 JPA 1", 100, 10);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), item.getItemId(), orderCount);

        orderService.cancelOrder(orderId);

        Order getOrder = orderRepository.findOne(orderId);

        Assertions.assertEquals( OrderStatus.CANCEL, getOrder.getStatus(),"주문 취소시 상태는 CANCEL 이다.");
        Assertions.assertEquals( 10, item.getStockQuantity(), "주문이 취소된 상품은 그만큼 재고가 증가해야 한다.");
    }
    private Member createMember(){
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "11938"));
        em.persist(member);
        return member;
    }

    private Book createBook(String name, int price, int stockQuantity){
        Book book = new Book();
        book.setName(name);
        book.setStockQuantity(stockQuantity);
        book.setPrice(price);
        em.persist(book);
        return book;
    }
}