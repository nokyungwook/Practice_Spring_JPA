package data.datajpa.repository;

import data.datajpa.entity.Member;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;
//사용자 정의 리포지토리 구현
// 주로 QueryDSL이나 SpringJdbcTemplate 사용할때 사용됨.
// 조건
// [엔티티]+RepositoryImpl 이라는 이름 조합으로 만들어야 JPA가 알아서 찾아서 만들어준다.
//xml 설정이나 Javaconfig 설정으로 Impl을 대신해서 다른 이름으로 바꿀수는 있으나 왠만해선 관례를 따르는게 좋다.
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}
