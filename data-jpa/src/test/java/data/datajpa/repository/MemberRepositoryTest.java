package data.datajpa.repository;

import data.datajpa.dto.MemberDto;
import data.datajpa.entity.Member;
import data.datajpa.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    EntityManager em;

    @Test
    public void testMember(){
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(member).isEqualTo(findMember);
    }
    @Test
    public void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        //카운트 검증
        long deletedcount = memberRepository.count();
        assertThat(deletedcount).isEqualTo(deletedcount);

    }

    @Test
    public  void findByUsernameAndAgeGreaterThan(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);

    }
    @Test
    public  void findHelloBy(){
        List<Member> helloBy = memberRepository.findTop3HelloBy();
    }

    @Test
    public  void testNamedQuery(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsername("AAA");
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public  void testQuery(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public  void testfindUserList(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> result = memberRepository.findUsernameList();
        for(String s : result){
            System.out.println("s = " + s);
        }
    }

    @Test
    public  void testMemberDto(){
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10);
        m1.setTeam(team);
        memberRepository.save(m1);


        List<MemberDto> result = memberRepository.findMemberDto();
        for(MemberDto dto : result){
            System.out.println("dto = " + dto);
        }
    }

    @Test
    public  void testfindByNames(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList( "AAA","BBB"));
        for(Member m : result){
            System.out.println("member = " + m);
        }
    }

    @Test
    public  void returnType(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //List<Member> aaa = memberRepository.findListByUsername("AAA"); //주의점! 컬렉션 타입이 반환타입인 경우 조회 결과가 없으면 null이 아닌 빈 컬렉션 객체를 반환한다.
        //Member findMember = memberRepository.findMemberByUsername("AAA"); //주의점! 단건인 경우 반환값이 없으면 null을 반환한다.

        //java 8부터 null 문제는 Optional로 해결된다.
        Optional<Member> findMember= memberRepository.findOptionalByUsername("AAA");
        System.out.println("findMember = " + findMember);
    }

    @Test
    public void paging() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));


        //when
        //Page는 offset(시작)이 0부터 시작이다.
        Page<Member> page = memberRepository.findByAge(age, pageRequest); //더보기 기능에 유용하게  사용한다.
        //실무Tip) 반드시 Entity를 노출시키면 안된다. DTO로 감싸야한다.
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        //Slice<Member> page = memberRepository.findByAge(age, pageRequest); //더보기 기능에 유용하게  사용한다.

        // 페이지 계산 공식 적용...
        // totalPage = totalCount / size ...
        // 마지막 페이지 ...
        // 최초 페이지 ...

        //then
        List<Member> content = page.getContent();
        //실무Tip) totalCount를 분리해서 쓴다 . @Query를 사용. join을 할 경우 count를 사용하는데 성능이슈가 있을수 있다.
        //long totalElements = page.getTotalElements();  //Page 사용시 제공해준다.

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();

    }

    @Test
    public void bulkUpdate() {
        memberRepository.save(new Member("member1", 11));
        memberRepository.save(new Member("member2", 12));
        memberRepository.save(new Member("member3", 23));
        memberRepository.save(new Member("member4", 24));
        memberRepository.save(new Member("member5", 45));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);

        //벌크연산의 영속성 context!!의 해결책
        //em.flush(); //변경되지 않은 남아잇는 내용을 DB에 반영해준다.
        //em.clear(); //영속성 Context를 비워준다. ==> @Modifying(clearAutomatically = true) 어노테이션으로 대체 가능

        //벌크연산의 주의해야할 점. 영속성 context!!
        List<Member> result = memberRepository.findByUsername("member5");
        Member member5 = result.get(0);
        System.out.println("member5 = " + member5);


        //then
        assertThat(resultCount).isEqualTo(3);

    }

    @Test
    public void findMemberLazy() {
        //given
        //member1 -> teamA
        //member2 -> teamB

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //when N + 1
        //select Member 1
        List<Member> members = memberRepository.findAll();
        //List<Member> members = memberRepository.findMemberFetchJoin();

        for(Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.teamClass = " + member.getTeam().getClass());
            System.out.println("member.team = " + member.getTeam().getName());
        }
    }

    @Test
    public void queryHint(){
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2"); //변경 시도시 무시됨. 이유는 메소드 어노테이션으로 QueryHint룰 readOnly로 설정하엿기때문이다.
        //findMember.setUsername("member2");

        em.flush(); //더티체킹 후 DB에 반영
    }

    @Test
    public void lock(){
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
        List<Member> findMember = memberRepository.findLockByUsername("member1");
    }

    @Test
    public void callCustom(){
        List<Member> ressult = memberRepository.findMemberCustom();
    }
}