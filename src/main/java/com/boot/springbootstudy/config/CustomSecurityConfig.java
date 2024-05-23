package com.boot.springbootstudy.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Log4j2
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) //메소드 수준의 보안 설정을 활성화하는 역할  / prePostEnabled 를 true로 설정 시 -> @PreAuthorize와 @PostAuthorize 어노테이션을 사용하여 사전 혹은 사후의 권한을 체크할 수 있습니다. 메소드 수준의 보안을 활성화할 수 있습니다.
//@PreAuthorize: 메소드 실행 전에 특정 조건을 검사하여 접근을 허용 또는 거부합니다.  /  @PostAuthorize: 메소드 실행 후에 특정 조건을 검사하여 결과를 반환하거나 변경합니다.
public class CustomSecurityConfig {

//  스프링은 기본적으로 빈을 싱글톤으로 관리합니다. 따라서 passwordEncoder() 메서드를 통해 생성된 BCryptPasswordEncoder 객체는 Spring IoC 컨테이너에 의해 단 한 번만 생성되고, 그 후에는 해당 빈이 필요한 곳에서 재사용됩니다.
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

// SecurityFilterChain : HttpSecurity 객체를 사용하여 보안 필터 체인을 구성합니다. 모든 요청을 인증하도록 설정하고, 사용자 정의 로그인 페이지와 로그아웃 기능을 설정합니다.
//   HTTP 요청 인증 및 인가: 요청을 인증하고 인가하는 필터를 설정합니다.
//   로그인/로그아웃: 로그인 페이지, 로그인 처리, 로그아웃 처리 등을 설정합니다.
//   보안 헤더 설정: XSS, CSRF 등의 공격을 방지하기 위한 보안 헤더를 설정합니다.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception{

        log.info("----------configure------------");

        http.formLogin().loginPage("/member/login");   //http.formLogin() : 로그인 화면에서 로그인을 진행한다는 설정. / loginPage() : 로그인 페이지 설정

        http.csrf().disable();  //CSRF토큰 비활성화

        return http.build();
    }


//  WebSecurityCustomizer : 보안 필터 체인을 구성하는 대신, 보안 설정에서 특정 요청을 완전히 무시하도록 설정하는 역할. 보안 필터 체인에 전혀 포함되지 않으며, 보안 검사를 받지 않습니다.
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){

        log.info("---------web configure ---------");

        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//web.ignoring() : (특정 요청을 보안 필터 체인에서 제외하도록 설정) 지정된 경로에 대한 요청이 보안 필터 체인을 통과하지 않고 무시됩니다.
// requestMatchers : 무시할 요청 매처를 지정
// PathRequest.toStaticResources().atCommonLocations() : 일반적으로 사용되는 정적 리소스 경로를 포함합니다
    }



}
