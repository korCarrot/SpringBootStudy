package com.boot.springbootstudy.controller.advice;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Log4j2
public class CustomRestAdvice {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED) //일반적으로 클라이언트가 "Expect" 헤더를 사용하여 요청에 대한 기대사항을 전달하였지만, 서버가 이를 충족시키지 못할 때 발생
    public ResponseEntity<Map<String, String>> handleBindException(BindException e){

        log.info(e);

        Map<String, String> errorMap = new HashMap<>();

        if (e.hasErrors()){
//            getBindingResult()는 스프링 MVC에서 사용되는 메서드로, 폼 데이터를 바인딩한 후에 발생한 바인딩 결과를 반환합니다. 주로 폼 입력 유효성 검증을 위해 사용됩니다
//            BindingResult 객체에는 발생한 오류에 대한 정보와 함께 어떤 필드에서 오류가 발생했는지 등의 세부 정보가 포함되어 있습니다
            BindingResult bindingResult = e.getBindingResult();

            bindingResult.getFieldErrors().forEach(fieldError -> {errorMap.put(fieldError.getField(), fieldError.getCode());
            });
        }
        return ResponseEntity.badRequest().body(errorMap);
    }
}
