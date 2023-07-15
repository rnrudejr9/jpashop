package jpabook.jpashop.repository;


import jpabook.jpashop.domain.items.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;


    /**
     * item은 저장하기 전까지 id 값이 없다 = 새로 생성한 객체
     * id가 있으면 = 수정된 객체
     * @param item
     */
    public void save(Item item){
        if(item.getId() == null) {
            em.persist(item);
        }
        else{
            em.merge(item);
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select  i from Item i",Item.class)
                .getResultList();
    }
}
