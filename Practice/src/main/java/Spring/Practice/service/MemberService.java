package Spring.Practice.service;

import Spring.Practice.domain.Member;
import Spring.Practice.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemberService {

	private final MemberRepository memberRepository;
	@Autowired
	public MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	@Transactional
	public Long join(Member member) {
		isDuplicateMember(member);
		memberRepository.save(member);
		return member.getId();
	}

	private void isDuplicateMember(Member member) {
		List<Member> members = memberRepository.findByName(member.getName());
		if (!members.isEmpty()) {
			throw new IllegalStateException("이미 존재하는 회원입니다.");
		}
	}

	public List<Member> findMembers() {
		return memberRepository.findAll();
	}

	public Member findOne(Long memberId) {
		return memberRepository.findOne(memberId);
	}
}
