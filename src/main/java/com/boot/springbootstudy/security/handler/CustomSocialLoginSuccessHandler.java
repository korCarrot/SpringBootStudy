package com.boot.springbootstudy.security.handler;

import com.boot.springbootstudy.security.dto.MemberSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//스프링 시큐리티의 로그인 성공과 실패 커스터마이징
@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final PasswordEncoder passwordEncoder;

    //로그인 성공시
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("----------------------------------------------------------");
        log.info("CustomLoginSuccessHandler onAuthenticationSuccess..............");
        log.info(authentication.getPrincipal());//Spring Security에서 인증된 사용자 정보를 반환

        //Authentication 객체 : 스프링 시큐리티에서 현재 인증된 사용자에 대한 정보를 담고 있는 객체
        //Principal: 인증된 사용자 정보        //Credentials: 인증에 사용된 자격 증명 (예: 비밀번호)        //Authorities: 사용자가 가진 권한 목록

        MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();

        String encodedPw = memberSecurityDTO.getMpw();

        //소셜 로그인이고 회원의 패스워드가 1111   //matches(rawPassword, encodedPassword) : 사용자가 입력한 비밀번호와 저장된 암호화된 비밀번호를 비교하여 일치 여부를 확인하는 데 사용
        // rawPassword: 사용자로부터 입력받은 원본 비밀번호입니다. 보통 로그인 시 입력된 비밀번호입니다.        //encodedPassword: 데이터베이스에 저장된 암호화된 비밀번호입니다.
        if (memberSecurityDTO.isSocial() && (memberSecurityDTO.getMpw().equals("1111") || passwordEncoder.matches("1111", memberSecurityDTO.getMpw()))) {

            log.info("Should Change Password");
            response.sendRedirect("/member/modify");

            return;

        }else{
            response.sendRedirect("/board/list");
        }
    }
}
