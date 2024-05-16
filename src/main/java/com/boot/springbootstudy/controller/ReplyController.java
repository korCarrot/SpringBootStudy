package com.boot.springbootstudy.controller;

import com.boot.springbootstudy.dto.ReplyDTO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/replies")
@Log4j2
public class ReplyController {

    //@ApiOperation은 Swagger 문서에서 개별 API 작업(메서드)에 대한 정보를 정의하는 데 사용되는 애노테이션입니다. 이를 사용하여 API 작업의 간단한 설명 및 세부 정보를 제공할 수 있습니다.
    //value: API 작업의 간단한 설명을 제공합니다. 이 값은 해당 작업이 무엇을 수행하는지를 간결하게 설명해야 합니다. 주로 작업의 이름 또는 요약을 제공합니다.
    //notes: API 작업에 대한 자세한 설명을 제공합니다. 이 값은 해당 작업의 세부 정보, 기능, 사용법 등을 자세히 설명해야 합니다. 주로 작업의 동작 방식이나 요청/응답 형식에 대한 정보를 포함합니다.
    @ApiOperation(value = "Replies POST", notes = "POST 방식으로 댓글 등록")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE) //consumes : 해당 메소드를 받아서 소비(consume)하는 데이터가 어떤 종류인지 명시 //요청 본문의 내용을 JSON 형식으로 읽어들임. (consumes = MediaType.APPLICATION_JSON_VALUE).
    public ResponseEntity<Map<String, Long>> register(@Valid @RequestBody ReplyDTO replyDTO, BindingResult bindingResult) throws BindException { //@RequestBody는 JSON 문자열을 ReplyDTO로 변환하기 위해서 표시

        log.info(replyDTO);

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        Map<String, Long> resultMap = Map.of("rno", 111L);

        return ResponseEntity.ok(resultMap);
    }

}