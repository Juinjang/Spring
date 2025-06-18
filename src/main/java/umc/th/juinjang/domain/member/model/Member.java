package umc.th.juinjang.domain.member.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.th.juinjang.domain.common.BaseEntity;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.note.liked.model.LikedNote;
import umc.th.juinjang.domain.note.shared.model.SharedNote;
import umc.th.juinjang.domain.pencil.purchased.model.PurchasedPencil;
import umc.th.juinjang.domain.pencil.used.model.UsedPencil;
import umc.th.juinjang.domain.pencilaccount.model.PencilAccount;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity implements UserDetails {

	@Id
	@Column(name = "member_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberId;

	@Column(nullable = false)
	private String email;

	private String nickname;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MemberProvider provider;

	@Column(name = "agree_version")
	private String agreeVersion;

	// apple client id값을 의미
	@Column(name = "apple_sub")
	private String appleSub;

	// kakao target id값 의미 (카카오의 유저 식별값. 탈퇴할 때 필요)
	@Column(name = "target_id")
	private Long kakaoTargetId;

	@Lob
	private String imageUrl;

	@Column(nullable = false)
	private String refreshToken;

	@Column(nullable = false)
	private LocalDateTime refreshTokenExpiresAt;

	private String introduction;

	@Enumerated(EnumType.STRING)
	private MemberStatus status;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<PencilAccount> pencilAccounts = new ArrayList<>();

	@OneToMany(mappedBy = "memberId", cascade = CascadeType.ALL, orphanRemoval = false)
	private List<Limjang> limjangList = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<PurchasedPencil> purchasedPencils = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<UsedPencil> usedPencils = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<SharedNote> sharedNotes = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<LikedNote> likedNotes = new ArrayList<>();

	public static Member createKakaoMember(String email, Long targetId, String nickname, String agreeVersion) {
		String introduction = String.format("안녕하세요, %s 입니다.", nickname);

		Member member = Member.builder()
			.email(email)
			.provider(MemberProvider.KAKAO)
			.kakaoTargetId(targetId)
			.nickname(nickname)
			.refreshToken("")
			.refreshTokenExpiresAt(LocalDateTime.now())
			.agreeVersion(agreeVersion)
			.introduction(introduction)
			.status(MemberStatus.ACTIVE)
			.build();

		PencilAccount createAccount = PencilAccount.createPencilAccount(member);
		member.addPencilAccount(createAccount);

		return member;
	}

	// 애플 회원 생성 팩토리 메서드
	public static Member createAppleMember(String email, String sub, String nickname, String agreeVersion) {
		String introduction = String.format("안녕하세요, %s 입니다.", nickname);

		Member member = Member.builder()
			.email(email)
			.nickname(nickname)
			.provider(MemberProvider.APPLE)
			.appleSub(sub)
			.refreshToken("")
			.refreshTokenExpiresAt(LocalDateTime.now())
			.agreeVersion(agreeVersion)
			.introduction(introduction)
			.status(MemberStatus.ACTIVE)
			.build();

		PencilAccount createAccount = PencilAccount.createPencilAccount(member);
		member.addPencilAccount(createAccount);

		return member;
	}

	// refreshToken 재발급
	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
		this.refreshTokenExpiresAt = LocalDateTime.now().plusDays(7);
	}

	// 로그아웃 시 토큰 만료
	public void refreshTokenExpires() {
		this.refreshToken = "";
		this.refreshTokenExpiresAt = LocalDateTime.now();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public void updateNickname(String nickname) {
		this.nickname = nickname;
	}

	public void updateImage(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public void updateAgreeVersion(final String agreeVersion) {
		this.agreeVersion = agreeVersion;
	}

	public PencilAccount getAccount() {
		if (this.pencilAccounts == null || this.pencilAccounts.isEmpty()) {
			return null;
		}

		return this.pencilAccounts.get(0);
	}

	public void addPencilAccount(PencilAccount pencilAccount) {
		if (this.pencilAccounts == null) {
			this.pencilAccounts = new ArrayList<>();
		}
		this.pencilAccounts.add(pencilAccount);
	}

	public void kakaoWithdraw() {
		this.status = MemberStatus.WITHDRAWN;
		this.kakaoTargetId = null;
	}

	public void appleWithdraw() {
		this.status = MemberStatus.WITHDRAWN;
		this.appleSub = null;
	}
}
