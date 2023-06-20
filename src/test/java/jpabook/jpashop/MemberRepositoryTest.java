package jpabook.jpashop;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


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