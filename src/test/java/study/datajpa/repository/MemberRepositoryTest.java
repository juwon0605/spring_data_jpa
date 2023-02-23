package study.datajpa.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

	@Autowired
	MemberRepository memberRepository;
	@Autowired
	TeamRepository teamRepository;

	@Test
	public void testMember() {
		System.out.println("memberRepository.getClass() = " + memberRepository.getClass());

		Member member = new Member("memberA");
		Member saveMember = memberRepository.save(member);

		Member findMember = memberRepository.findById(saveMember.getId()).get();

		assertThat(findMember.getId()).isEqualTo(member.getId());
		assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
		assertThat(findMember).isEqualTo(member);
	}

	@Test
	public void basicCRUD() {
		Member member1 = new Member("member1");
		Member member2 = new Member("member2");
		memberRepository.save(member1);
		memberRepository.save(member2);

		//단건 조회 검증
		Member findMember1 = memberRepository.findById(member1.getId()).get();
		Member findMember2 = memberRepository.findById(member2.getId()).get();
		assertThat(findMember1).isEqualTo(member1);
		assertThat(findMember2).isEqualTo(member2);

		findMember1.setUsername("change member");

		// //리스트 조회 검증
		List<Member> all = memberRepository.findAll();
		assertThat(all.size()).isEqualTo(2);

		//카운트 검증
		long count = memberRepository.count();
		assertThat(count).isEqualTo(2);

		//삭제 검증
		memberRepository.delete(member1);
		memberRepository.delete(member2);

		long deletedCount = memberRepository.count();
		assertThat(deletedCount).isEqualTo(0);
	}

	@Test
	public void findByUsernameAndAgeGreaterThen() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("AAA", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

		assertThat(result.get(0).getUsername()).isEqualTo("AAA");
		assertThat(result.get(0).getAge()).isEqualTo(20);
		assertThat(result.size()).isEqualTo(1);
	}

	@Test
	public void findHelloBy() {
		List<Member> helloBy = memberRepository.findTop3HelloBy();
	}

	@Test
	public void testNamedQuery() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		List<Member> result = memberRepository.findByUsername("AAA");
		Member findMember = result.get(0);
		assertThat(findMember).isEqualTo(m1);
	}

	@Test
	public void testQuery() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		List<Member> result = memberRepository.findUser("AAA", 10);
		assertThat(result.get(0)).isEqualTo(m1);
	}

	@Test
	public void findUsernameList() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		List<String> usernameList = memberRepository.findUsernameList();
		for (String s : usernameList) {
			System.out.println("s = " + s);
		}
	}

	@Test
	public void findMemberDto() {
		Team team = new Team("teamA");
		teamRepository.save(team);

		Member m1 = new Member("AAA", 10);
		m1.setTeam(team);
		memberRepository.save(m1);

		List<MemberDto> memberDtos = memberRepository.findMemberDto();
		for (MemberDto memberDto : memberDtos) {
			System.out.println("memberDto = " + memberDto);
		}
	}

	@Test
	public void findByNames() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
		for (Member member : result) {
			System.out.println("member = " + member);
		}
	}

	@Test
	public void returnType() {
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		Member m3 = new Member("AAA", 30);
		memberRepository.save(m1);
		memberRepository.save(m2);
		memberRepository.save(m3);

		// 사용법
		List<Member> result1 = memberRepository.findListByUsername("AAA");
		System.out.println("result1 = " + result1);
		// result1 = [Member(id=1, username=AAA, age=10), Member(id=3, username=AAA, age=30)]

		Member result2 = memberRepository.findMemberByUsername("BBB");
		System.out.println("result2 = " + result2);
		// result2 = Member(id=2, username=BBB, age=20)

		Optional<Member> reesult3 = memberRepository.findOptionalByUsername("BBB");
		System.out.println("result3 = " + reesult3);
		// result3 = Optional[Member(id=2, username=BBB, age=20)]

		//주의 사항
		List<Member> result4 = memberRepository.findListByUsername("CCC");
		System.out.println("result4 = " + result4);
		// result4 = []

		Member result5 = memberRepository.findMemberByUsername("CCC");
		System.out.println("result5 = " + result5);
		// result5 = null

		Optional<Member> reesult6 = memberRepository.findOptionalByUsername("CCC");
		System.out.println("result6 = " + reesult6);
		// result6 = Optional.empty

		//예외 발생
		// Member result7 = memberRepository.findMemberByUsername("AAA");
		// Optional<Member> result8 = memberRepository.findOptionalByUsername("AAA");
		// org.springframework.dao.IncorrectResultSizeDataAccessException: query did not return a unique result:
	}
}
