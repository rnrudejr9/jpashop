package jpabook.jpashop;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;


//자동 스프링 빈 등록
@Repository
public class MemberRepository {
    @PersistenceContext
    private EntityManager em;
    //스프링부트에서 em을 주입을 해줌
    public Long save(Member member){
        em.persist(member);
        return member.getId();
    }

    public Member find(Long id){
        return em.find(Member.class,id);
    }


}
