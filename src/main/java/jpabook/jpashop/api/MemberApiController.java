package jpabook.jpashop.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    /**
     *
     * @RequestBody JSON으로 온 데이터를 매개변수로 바꿔준다 ( springMVC )
     * @param member Validation 적용
     * @return
     */
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     * entity 필드 명을 수정해도 api 스펙이 바뀌지 않는다(set만 수정하면 된다)
     * @param request
     * @return
     */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     * 변경 메소드는 변경만 하게끔 member 리턴하지 않게 한다.
     * @param id
     * @param request
     * @return
     */
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id, @RequestBody @Valid UpdateMemberRequest request){
        memberService.update(id,request.getName());
        Member member = memberService.findOne(id);
        return new UpdateMemberResponse(member.getName(),member.getId());
    }

    @GetMapping

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse{
        private String name;
        private Long id;
    }

    @Data
    static class UpdateMemberRequest{
        private String name;
    }


    @Data
    static class CreateMemberRequest{
        @NotEmpty
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
