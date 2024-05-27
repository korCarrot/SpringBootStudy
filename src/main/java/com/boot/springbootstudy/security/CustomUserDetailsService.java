package com.boot.springbootstudy.security;

import com.boot.springbootstudy.domain.Member;
import com.boot.springbootstudy.repository.MemberRepository;
import com.boot.springbootstudy.security.dto.MemberSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {   //UserDetailsService : 실제로 인증을 처리하는 인터페이스의 구현체

    private final MemberRepository memberRepository;

//    private PasswordEncoder passwordEncoder;

//    public CustomUserDetailsService(){
//        this.passwordEncoder = new BCryptPasswordEncoder(); //CustomSecurityConfig의 싱글톤 Bean객체로 주입. (생성자 내부에서 직접 주입 방식)
//    }

//  loadUserByUsername : 실제 인증을 처리할 때 호출되는 부분 (UserDetailsService의 유일한 메소드)
//  UserDetails : 사용자 인증(Authentication)과 관련된 정보들을 저장하는 역할
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("loadUserByUsername: " + username);

                                // User : 스프링 시큐리티 API에서 UserDetails 인터페이스를 구현한 클래스
//        UserDetails userDetails = User.builder()
//                                    .username("user1")
//                                     .password("1111")
//                                    .password(passwordEncoder.encode("1111"))   //패스워드 인코딩 필요
//                                    .authorities("ROLE_USER")
//                                    .build();

//        return userDetails;

        Optional<Member> result = memberRepository.getWithRoles(username);

        if (result.isEmpty()) { //해당 아이디를 가진 사용자가 없다면
            throw new UsernameNotFoundException("username not found...");
        }

        Member member = result.get();   //result.get();은 Optional 객체에서 Member 객체를 꺼내옵니다.

        MemberSecurityDTO memberSecurityDTO=
                new MemberSecurityDTO(
                        member.getMid(),
                        member.getMpw(),
                        member.getEmail(),
                        member.isDel(),
                        false,  //SimpleGrantedAuthority : GrantedAuthority 인터페이스(사용자 권한)의 구현 클래스
                        //SimpleGrantedAuthority 생성자는 권한 이름을 인수로 받아 새로운 SimpleGrantedAuthority 객체를 생성합니다. 이 권한 이름은 보통 ROLE_USER, ROLE_ADMIN과 같은 형식을 따릅니다.
                        member.getRoleSet().stream().map(memberRole -> new SimpleGrantedAuthority("ROLE_"+memberRole.name()))
                                .collect(Collectors.toList())
                );

        log.info("memberSecurityDTO: "+ memberSecurityDTO);

        return memberSecurityDTO;

    }
}
