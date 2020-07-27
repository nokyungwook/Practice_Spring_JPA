package data.datajpa.controller;

import data.datajpa.dto.MemberDto;
import data.datajpa.entity.Member;
import data.datajpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    //PathVariable에 id로 잘 사용하진 않는다. pk가 노출되기 때문이다. 조회용!이라면 간단하게 쓸 수 있다.
    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id")Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    //도메인 클래스 컨버터
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id")Member member){
        return member.getUsername();
    }

    //@PageableDefault 어노테이션으로 size와 정렬 순서를 정할수 있다.
    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5) Pageable pageable){
        return memberRepository.findAll(pageable)
                .map(MemberDto::new);
    }

    @PostConstruct
    public void init(){
        //memberRepository.save(new Member("userA"));
        for(int i = 0; i < 100; i++){
            memberRepository.save(new Member("user" + i,i));
        }
    }

}
