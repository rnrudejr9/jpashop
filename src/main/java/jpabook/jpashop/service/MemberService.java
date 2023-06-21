package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


//컴포넌트 스캔의 대상 + 스프링 빈 등록
@Service
@Transactional(readOnly = true)
//데이터 변경간 트랜잭션 있어야됨
//javax, spring 제공 어노테이션이 있는데, spring게 쓸수 있는 옵션이 많음
//readOnly면 읽기처리만해서 성능 최적화가 됨, 하지만 정보 수정할 것들이 있으면 그 메소드는 어노테이션을 붙여줘야됨
@RequiredArgsConstructor
//final 있는 필드만 생성자를 만들어준다.
public class MemberService {

    private final MemberRepository memberRepository;
    //final 컴파일 시점 오류 확인 가능
    //생성자 하나일 경우 자동 주입됨

    //회원가입
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //exception
        //aws 두개 서버 운영시 중복으로 가입 될 수 있어서 DB 차원에서 unique 제약조건을 걸어두면 좋음
        List<Member> members = memberRepository.findByName(member.getName());
        if(!members.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }



    //전체조회
    //트랜잭션간 성능 최적화됨, 단순 읽기용 모드로 읽어
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }


    /**
     * 변경감지로 인한 엔티티 변경
     * @param id
     * @param name
     */
    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
