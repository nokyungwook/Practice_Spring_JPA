package data.datajpa.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass //상속시 속성만 공유하게 해주는 어노테이션
public class JpaBaseEntity {
    @Column(updatable = false) //변경되지 못하게 설정 ㅇefault: insertabe=true
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;

    @PrePersist
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        createDateTime = now;
        updateDateTime = now;
    }

    @PreUpdate
    public void preUpdate(){
        updateDateTime = updateDateTime.now();
    }

}
