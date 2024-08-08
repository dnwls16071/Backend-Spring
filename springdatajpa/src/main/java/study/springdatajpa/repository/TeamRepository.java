package study.springdatajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.springdatajpa.domain.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
