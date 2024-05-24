package com.boot.springbootstudy.security.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//403에러 : 서버에서 사용자의 요청을 거부 (권한이 없거나 특정 조건이 맞지 않는 경우 등이 해당)
@Log4j2     //AccessDeniedHandler 접근이 거부된 경우(403 에러) 처리해야 할 작업 정의
public class Custom403Handler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        log.info("-----ACCESS DENIED-----");

        response.setStatus(HttpStatus.FORBIDDEN.value());

        //JSON 요청이었는지 확인
        String contentType = request.getHeader("Content-Type");

        boolean jsonRequest = contentType.startsWith("application/json");

        log.info("isJSON: " + jsonRequest);

        //일반 request (JSON이 아닌 경우)
        if(!jsonRequest){
            response.sendRedirect("/member/login?error=ACCESS_DENIED"); //member 앞에 / 작성 안하면 board까지 나오는 링크가 되니까 주의.
        }

    }
}
