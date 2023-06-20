package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest
@Transactional
//여러 테스트를 위한 롤백
class MemberServiceTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Test
//    @Rollback(false)
    //persist 한다고 해서 commit 되는 것이 아니다.
    void 회원가입() {
        //given : 주어졌는데
        Member member = new Member();
        member.setName("SpringmyLog");

        //when : 이렇게 실행하면
        Long join = memberService.join(member);

        //then : 이런 결과가 나와야해

        assertEquals(member,memberRepository.findOne(join));
    }

    @Test
    void 중복회원예외() throws Exception{
        //given
        Member member = new Member();
        member.setName("spring");
        Member member1 = new Member();
        member1.setName("spring");
        //when
        memberService.join(member);


        //then
        assertThrows(IllegalStateException.class,()->{
            memberService.join(member1);
        });
    }

}