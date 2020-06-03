package data.datajpa.repository;

import data.datajpa.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamJpaRepository extends JpaRepository<Team,Long> {
}
