package data.datajpa.dto;

import data.datajpa.entity.Member;
import lombok.Data;

@Data
public class MemberDto {

    private Long id;
    private String username;
    private String teamname;

    public MemberDto(Long id, String username, String teamname) {
        this.id = id;
        this.username = username;
        this.teamname = teamname;
    }

    //DTO는 entity를 볼수 있지만 entity는 DTO를 볼수 없게 해야한다.
    public  MemberDto(Member member){
        this.id = member.getId();
        this.username = member.getUsername();
    }
}
