package com.boot.springbootstudy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    @GetMapping("/login")   //명시적으로는 @RequesParam을 써주는게 좋은듯?
    public void loginGET(String error, String logout){  //logout 파라미터는 스프링 시큐리티의 기본 로그아웃 설정에 의해 로그아웃 성공 후 리디렉션되는 URL (/login?logout)에서 전달됩니다
        log.info("login get.......");
        log.info("logout: " + logout);

        if (logout != null) {
            log.info("user logout......");
        }
    }

}
