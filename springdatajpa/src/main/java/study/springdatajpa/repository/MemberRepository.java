package study.springdatajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.springdatajpa.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
