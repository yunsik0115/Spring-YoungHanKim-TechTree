package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded // Internal Type was used (both side mentioning is recommended)
    private Address address;

    // mappedBy order 테이블에 있는 member 필드에 의해서 mapping 된 것임.(READ ONLY)
    // INSERTING VALUE IN HERE DOES NOT CHANGE THE FK.
    @OneToMany(mappedBy = "member") // 주인이 아닌 부분에 mappedby 작성
    @JsonIgnore
    private List<Order> orders = new ArrayList<>();
    // DB's foreign Key -> MEMBER_ID(FK) in ORDER TABLE ONLY
    // MEMBER에도 FIELD ORDER에도 FIELD WHICH ONE SHOULD BE CHECKED FOR CHANGES?
    // OBJECT - 2 change point, DB - Only 1 change point(FK)
    // THEREFORE MAPPED BY -> IF THIS ONE CHANGED THEH FK SHOULD BE UPDATED

}
