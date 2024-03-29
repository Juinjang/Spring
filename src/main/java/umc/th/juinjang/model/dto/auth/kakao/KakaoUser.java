package umc.th.juinjang.model.dto.auth.kakao;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KakaoUser {
    // 다시 JSON 형식의 응답 객체를 deserialization 해서 자바 객체에 담음
    // -> 여기서 자바 객체에 해당하는 클래스
    private Long id;
    private KakaoAccount kakao_account;

    @Data
    public class KakaoAccount {
        public Boolean profile_nickname_needs_agreement;
        public Profile profile;
        public Boolean has_email;
        public Boolean email_needs_agreement;
        public Boolean is_email_valid;
        public Boolean is_email_verified;
        public String email;

        @Data
        public class Profile {
            private String nickname;
        }

    }

}
