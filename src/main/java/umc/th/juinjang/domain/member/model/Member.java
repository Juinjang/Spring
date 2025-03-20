package umc.th.juinjang.domain.member.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import umc.th.juinjang.domain.limjang.model.Limjang;
import umc.th.juinjang.domain.common.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity implements UserDetails {

  @Id
  @Column(name="member_id")
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
  @Column(name = "apple_sub", unique = true)
  private String appleSub;

  // kakao target id값 의미 (카카오의 유저 식별값. 탈퇴할 때 필요)
  @Column(name="target_id", unique = true)
  private Long kakaoTargetId;

  @Lob
  private String imageUrl;

  @Column(nullable = false)
  private String refreshToken;

  @Column(nullable = false)
  private LocalDateTime refreshTokenExpiresAt;

  @OneToMany(mappedBy = "memberId", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Limjang> limjangList = new ArrayList<>();

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

  public void updateAgreeVersion(final String agreeVersion) { this.agreeVersion = agreeVersion; }
}