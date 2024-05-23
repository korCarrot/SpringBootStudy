package com.boot.springbootstudy.security;

import com.boot.springbootstudy.config.CustomSecurityConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
//@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {   //UserDetailsService : 실제로 인증을 처리하는 인터페이스의 구현체

    private PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(){
        this.passwordEncoder = new BCryptPasswordEncoder(); //CustomSecurityConfig의 싱글톤 Bean객체로 주입. (생성자 내부에서 직접 주입 방식)
    }

//  loadUserByUsername : 실제 인증을 처리할 때 호출되는 부분 (UserDetailsService의 유일한 메소드)
//  UserDetails : 사용자 인증(Authentication)과 관련된 정보들을 저장하는 역할
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("loadUserByUsername: " + username);

                                // User : 스프링 시큐리티 API에서 UserDetails 인터페이스를 구현한 클래스
        UserDetails userDetails = User.builder()
                                    .username("user1")
                                    // .password("1111")
                                    .password(passwordEncoder.encode("1111"))   //패스워드 인코딩 필요
                                    .authorities("ROLE_USER")
                                    .build();

        return userDetails;
    }
}
