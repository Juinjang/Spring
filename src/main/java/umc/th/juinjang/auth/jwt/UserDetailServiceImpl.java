package umc.th.juinjang.auth.jwt;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import umc.th.juinjang.common.ExceptionHandler;
import umc.th.juinjang.common.code.status.ErrorStatus;
import umc.th.juinjang.domain.member.repository.MemberRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;


    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        System.out.println("로그인한 memberId : " + memberId);
        UserDetails result = (UserDetails) memberRepository.findById(Long.parseLong(memberId))
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.MEMBER_NOT_FOUND));
        log.info("UserDetails: 여기ㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣ");
        //로그인할 때  result.getUsername() 여기서 에러남
//        log.info("UserDetails: " + result.getUsername());
        log.info("UserDetails: " + result.toString());

        return result;
    }
}
