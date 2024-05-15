package com.boot.springbootstudy.service;

import com.boot.springbootstudy.dto.BoardDTO;
import com.boot.springbootstudy.dto.PageRequestDTO;
import com.boot.springbootstudy.dto.PageResponseDTO;
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


    //글 수정 테스트
    @Test
    public void testModify() {

        //변경에 필요한 데이터만
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(201L)
                .title("Updated....201")
                .content("Updated content 201...")
                .build();

        boardService.modify(boardDTO);

    }

    //목록/검색 기능 확인 : PageRequestDTO 객체 -> BoardService -> BoardRepository - Page객체 -> BoardService - PageResponseDTO객체
    @Test
    public void testList() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")
                .keyword("1")
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

        log.info(responseDTO);

    }
}
