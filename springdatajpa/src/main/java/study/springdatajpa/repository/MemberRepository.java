package study.springdatajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.springdatajpa.domain.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

	@Query("select m from Member m where m.username = :username and m.age = :age")
	List<Member> findUser(@Param(value = "username") String username, @Param(value = "age") int age);
}
