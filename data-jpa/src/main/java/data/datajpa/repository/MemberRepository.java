package data.datajpa.repository;


import data.datajpa.dto.MemberDto;
import data.datajpa.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long>,MemberRepositoryCustom {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();

    //@Query(name="Member.findByUsername") 생략가능 NamedQuery가 우선 순위가 더 높으므로 알아서 찾아줌.
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member>findUser(@Param("username") String username,@Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    // DTO조회하기 new + 패키지명을 다 적어줘야함.
    @Query("select new data.datajpa.dto.MemberDto(m.id, m.username,t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    //파라미터 바인딩 - in절
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username); //컬렉션
    Member findMemberByUsername(String username); //단건
    Optional<Member> findOptionalByUsername(String username); //단건 Optional

    @Query(value = "select m from Member m left join m.team t", countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);
    //Slice<Member> findByAge(int age, Pageable pageable);
    //List<Member> findByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true) //이 어노테이션이 있어야 executeUpdate 함수가 실행된다.
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    //JPQL + 패치 조인
    @EntityGraph(attributePaths = {"team"})
    @Query("select  m from Member m")
    List<Member> findMemberEntityGraph();

    //패치 조인 네임드로 사용하기
    //@EntityGraph(attributePaths = {"team"})
    @EntityGraph("Member.all") //Member Entity에 애노테이션 사용
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    //성능테스트 후에 사용할지 결정해야할 것. 보통은 잘 안씀.
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    //select for update
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

}
