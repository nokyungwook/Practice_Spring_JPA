package data.datajpa.entity;

import data.datajpa.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void testEntity(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        entityManager.persist(teamA);
        entityManager.persist(teamB);

        Member member1 = new Member("member1",10, teamA);
        Member member2 = new Member("member2",20, teamA);
        Member member3 = new Member("member3",30, teamB);
        Member member4 = new Member("member4",40, teamB);

        entityManager.persist(member1);
        entityManager.persist(member2);
        entityManager.persist(member3);
        entityManager.persist(member4);

        //초기화
        entityManager.flush();
        entityManager.clear();

        //확인
        List<Member> members = entityManager.createQuery("select m from Member m", Member.class).getResultList();
        for(Member member : members){
            System.out.println("member : " + member);
            System.out.println("->member.team : " + member.getTeam());
        }
    }
    @Test
    public void JpaEventBaseEntity() throws Exception{
        //given
        Member member = new Member("member1");
        memberRepository.save(member); //@PrePersist

        Thread.sleep(100);  //테스트를 하기위 한 용도. 실무에서는 이렇게 사용하면 안됨.
        member.setUsername("member2");

        entityManager.flush();
        entityManager.clear();

        //when
        Member findMember = memberRepository.findById(member.getId()).get();

        //then
        System.out.println("findMember.createdDate =" + findMember.getCreateDate());
        System.out.println("findMember.lastModifiedDate =" + findMember.getLastModifiedDate());
        System.out.println("findMember.createBy =" + findMember.getCreateBy());
        System.out.println("findMember.lastModifiedBy =" + findMember.getLastModifiedBy());
    }

}