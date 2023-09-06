package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item){
        // == 처음에 저장할때는 id 없음 == //
        if(item.getItemId() == null){
            em.persist(item); // 쌩 신규
        } else {
            em.merge(item); // 등록된걸 어디서 가져오는 경우 //
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findByName(String name){
        return em.createQuery("select i from Item i where i.name = :name")
                .setParameter("name", name)
                .getResultList();
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i").getResultList();
    }




}
