package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.*;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "item")
@Getter
@Setter
// Inheritance Strategy Set
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Ramping All Attributes into single table.
@DiscriminatorColumn(name="dtype") // dtype -> mean for discriminate the type
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long itemId;

    private String name; // Common Attributes
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();


    //== Business Logic == //
    //== place it inside the entity(recommended) ==//
    public void addStockQuantity(int quantity){
        this.stockQuantity += quantity;
        // if change require needs specific business logic(not directly setter)
        // and also validation.
    }

    public void reduceStockQuantity(int quantity){
        int restStock = this.stockQuantity - quantity;
        if(restStock<0){
            throw new NotEnoughStockException("Not enough stock");
        }

        this.stockQuantity = restStock;
    }




}
