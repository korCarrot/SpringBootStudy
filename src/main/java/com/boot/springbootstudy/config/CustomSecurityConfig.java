package com.boot.springbootstudy.config;

import com.boot.springbootstudy.security.CustomUserDetailsService;
import com.boot.springbootstudy.security.handler.Custom403Handler;
import com.boot.springbootstudy.security.handler.CustomSocialLoginSuccessHandler;
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
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true) //메소드 수준의 보안 설정을 활성화하는 역할  / prePostEnabled 를 true로 설정 시 -> @PreAuthorize와 @PostAuthorize 어노테이션을 사용하여 사전 혹은 사후의 권한을 체크할 수 있습니다. 메소드 수준의 보안을 활성화할 수 있습니다.
//@PreAuthorize: 메소드 실행 전에 특정 조건을 검사하여 접근을 허용 또는 거부합니다.  /  @PostAuthorize: 메소드 실행 후에 특정 조건을 검사하여 결과를 반환하거나 변경합니다.
public class CustomSecurityConfig {

    //주입 필요
    private final DataSource dataSource;
    private final CustomUserDetailsService userDetailsService;

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
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new CustomSocialLoginSuccessHandler(passwordEncoder());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception{

        log.info("----------configure------------");

        //커스텀 로그인 페이지
        http.formLogin().loginPage("/member/login");   //http.formLogin() : 로그인 화면에서 로그인을 진행한다는 설정. / loginPage() : 로그인 페이지 설정

        //CSRF 토큰 비활성화
        http.csrf().disable();  //CSRF토큰 비활성화

        //브라우저를 닫았다가 다시 열더라도 로그인이 유지되도록 하는 기능
        http.rememberMe()   //사용자가 로그인할 때 "Remember Me" 옵션을 선택하면, 로그인 세션이 만료된 후에도 쿠키를 통해 사용자를 인증할 수 있게 합니다.
                .key("12345678")    //토큰을 생성하고 검증할 때 사용됩니다. 고유하고 예측 불가능한 값을 사용하는 것이 좋습니다.
                .tokenRepository(persistentTokenRepository())   //Remember Me 토큰을 저장하고 조회할 PersistentTokenRepository를 설정.
                .userDetailsService(userDetailsService) // Remember Me 토큰을 사용할 때 사용자 정보를 조회할 UserDetailsService를 설정. 이 서비스는 사용자 이름(ID)을 기반으로 사용자 정보를 로드
                .tokenValiditySeconds(60*60*24*30); //Remember Me 토큰의 유효 기간을 초 단위로 설정

        //403에러 예외 처리 (접근이 거부된 경우이기 때문)
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());

        //OAuth2 로그인을 사용
        http.oauth2Login()
                .loginPage("/member/login")
                .successHandler(authenticationSuccessHandler());

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

    //쿠키를 저장할 데이터베이스 반환하는 듯
    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new Custom403Handler();  //403 예외처리 한 클래스 객체 (AccessDeniedHandler를 구현한 객체임)
    }
}