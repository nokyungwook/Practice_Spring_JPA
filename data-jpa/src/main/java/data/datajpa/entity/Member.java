package data.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id,username","age"}) //연관관계 필드는 사용하지 않는다. 무한루프 가능성이 있다.
@NamedQuery(  //NameQuery의 유일한 장점으로는 오타를 잡아주는 것이다. 애플리케이션 로딩 시점에 파싱 해봄으로 오류 잡아준다.
        name="Member.findByUsername",
        query="select m from Member m where m.username = :username"
)
@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("tema"))
public class Member extends BaseEntity {
    @Id @GeneratedValue
    @Column(name="member_id")
    private long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="team_id")
    private Team team;

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if(team != null){
            changeTeam(team);
        }
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }

//    public void changeUsername(String username){
//        this.username = username;
//    }
}
