package study.springdatajpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.springdatajpa.domain.Member;
import study.springdatajpa.dto.MemberDto;
import study.springdatajpa.repository.custom.MemberRepositoryCustom;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

	@Query("select m from Member m where m.username = :username and m.age = :age")
	List<Member> findUser(@Param(value = "username") String username, @Param(value = "age") int age);

	@Query(value = "select new study.springdatajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
	List<MemberDto> findUserDto();

	@Query(value = "select m from Member m where m.username in :names")
	List<Member> findByNames(@Param(value = "names") List<String> names);

	// count 쿼리 분리해서 사용할 수 있음 + Page : count 쿼리 사용
	// 데이터는 left join, 카운트는 left join X
	@Query(value = "select m from Member m left join m.team t",
			countQuery = "select count(m) from Member m")
	Page<Member> findByAge(int age, Pageable pageable);

	// 벌크성 수정 쿼리
	@Modifying(clearAutomatically = true)	// 수정 시 필수
	@Query(value = "update Member m set m.age = m.age + 1 where m.age >= :age")
	int bulkAgePlus(@Param(value = "age") int age);

	// 페치조인 쿼리 작성 → 기본적으로 left outer join
	// join → team_id가 null인 member들이 안 나옴
	// left join → team_id가 null인 member들도 같이 나옴
	// left join(외부 조인의 개념) != join(내부 조인의 개념)
	@Query(value = "select m from Member m left join fetch m.team t")	// JPQL로 직접 페치 조인을 작성하여 해결하는 방법
	List<Member> findMemberFetchJoin();

	// 메서드 재정의해서 FETCH JOIN 적용
	@Override
	@EntityGraph(attributePaths = {"team"})
	List<Member> findAll();

	@QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
	Member findReadOnlyByUsername(@Param(value = "username") String username);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	List<Member> findLockByUsername(@Param("username") String username);
}
