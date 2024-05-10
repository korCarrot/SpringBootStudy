package com.boot.springbootstudy.service;

import com.boot.springbootstudy.dto.BoardDTO;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    //등록 처리 구현
    //BoardServiceImpl을 감싸는 클래스 정보가 출력 (이유 : @Transactional)
    @Test
    public void testRegister(){
        log.info(boardService.getClass().getName());    //BoardServiceImpl 클래스의 이름을 나오게 하는 코드

        BoardDTO boardDTO = BoardDTO.builder()
                .title("Sample title...")
                .content("Sample content...")
                .writer("user00")
                .build();

        long bno = boardService.register(boardDTO);

        log.info("bno: " + bno);
    }

    //글 하나 조회
    @Test
    public void getOne(){

        Long bno = 50L;

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);
    }

}
