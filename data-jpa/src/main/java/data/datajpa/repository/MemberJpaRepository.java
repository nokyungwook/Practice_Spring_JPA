package data.datajpa.repository;

import data.datajpa.entity.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {
    @PersistenceContext
    private EntityManager em;

    public Member save(Member member){
        em.persist(member);
        return member;
    }
    public void delete(Member member){
        em.remove(member);
    }

    public List<Member> findAll(){
        //JPQL
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public Optional<Member> findbyId(Long id){
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public long count(){
        return em.createQuery("select count(m) from Member m", Long.class)
                .getSingleResult();
    }

    public Member find(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age){
        return em.createQuery("select m from Member m where m.username = :username and m.age > :age")
                .setParameter("username", username)
                .setParameter("age",age)
                .getResultList();
    }

    public List<Member> findByusername(String username) {
        return em.createNamedQuery("Member.findByUsername", Member.class)
                .setParameter("username", "회원1")
                .getResultList();
    }

    public List<Member> findByPage(int age,int offset, int limit){
        return em.createQuery("select m from Member m where m.age = :age order by m.username desc ")
                .setParameter("age", age) //기준으로
                .setFirstResult(offset) //몆번째 부터
                .setMaxResults(limit) // 어디까지
                .getResultList();
    }

    public long totalCount(int age){
        return em.createQuery("select count(m) from Member m where m.age = :age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }

    public  int bulkAgePlus(int age){
        return em.createQuery("update Member m set m.age = m.age + 1 where  m.age >= :age")
                .setParameter("age",age)
                .executeUpdate();
    }
}
