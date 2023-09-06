package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="delivery")
@Getter
@Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY) // OpenSessionInView(Can Fetch faster before transaction. for special use)
    // OneToOne Focus Point = Where should FK located?
    // Locating it on the table which will be more frequently accessed.
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    // Ordinal Prohibited for future change(If value Inserted or Deleted, Value Integrity Not Ensured)
    private DeliveryStatus deliveryStatus;


}
