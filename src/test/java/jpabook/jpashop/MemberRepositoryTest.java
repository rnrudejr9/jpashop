package jpabook.jpashop;

import jakarta.annotation.security.RunAs;
import jpabook.jpashop.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    @Transactional
//    @Rollback(false) 데이터 삽입 확인
    //테스트 끝난 이후 롤백함, repo에 트랜잭션 없어도 사용가능
    void Member추가() {
        Member member = new Member();
        member.setUsername("Spring");

        Long save = memberRepository.save(member);
        Member findMember = memberRepository.find(save);

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);
//        같은 영속성 컨텍스트 안에서는 같은 엔티티로 식별함
    }

    @Test
    void find() {
    }
}