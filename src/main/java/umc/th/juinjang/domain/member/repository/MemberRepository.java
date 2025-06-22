package umc.th.juinjang.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import umc.th.juinjang.domain.member.model.Member;
import umc.th.juinjang.domain.member.model.MemberStatus;

public interface MemberRepository extends JpaRepository<Member, Long> {

	Optional<Member> findByEmail(String email);

	Optional<Member> findByRefreshToken(String refreshToken);

	Optional<Member> findByKakaoTargetId(Long targetId);

	Member findByNickname(String nickname);

	Optional<Member> findByAppleSub(String sub);

	@Modifying
	@Query("UPDATE Member m SET m.introduction = :introduction WHERE m.memberId = :id")
	void patchIntroduction(@Param("id") Long id, @Param("introduction") String introduction);

	boolean existsByNickname(String nickname);

	Optional<Member> findByEmailAndKakaoTargetIdAndStatus(String email, Long kakaoTargetId, MemberStatus status);

	Optional<Member> findByEmailAndAppleSubAndStatus(String email, String sub, MemberStatus status);

	Optional<Object> findByMemberIdAndStatus(long id, MemberStatus memberStatus);
}
