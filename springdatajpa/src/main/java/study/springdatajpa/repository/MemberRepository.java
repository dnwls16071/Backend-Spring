package study.springdatajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.springdatajpa.domain.Member;
import study.springdatajpa.dto.MemberDto;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

	@Query("select m from Member m where m.username = :username and m.age = :age")
	List<Member> findUser(@Param(value = "username") String username, @Param(value = "age") int age);

	@Query(value = "select new study.springdatajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
	List<MemberDto> findUserDto();

	@Query(value = "select m from Member m where m.username in :names")
	List<Member> findByNames(@Param(value = "names") List<String> names);
}
