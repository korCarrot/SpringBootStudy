package com.boot.springbootstudy.service;

import com.boot.springbootstudy.dto.*;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
//    @Test
//    public void testModify() {
//
//        //변경에 필요한 데이터만
//        BoardDTO boardDTO = BoardDTO.builder()
//                .bno(201L)
//                .title("Updated....201")
//                .content("Updated content 201...")
//                .build();
//
//        boardService.modify(boardDTO);
//
//    }

    @Test
    public void testModify() {

        //변경에 필요한 데이터
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(101L)
                .title("Updated....101")
                .content("Updated content 101...")
                .build();

        //첨부파일을 하나 추가
        boardDTO.setFileNames(Arrays.asList(UUID.randomUUID()+"_zzz.jpg"));

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

    @Test
    public void testRegisterWithImages() {

        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("File...Sample Title...")
                .content("Sample Content...")
                .writer("user00")
                .build();

        boardDTO.setFileNames(
                Arrays.asList(
                        UUID.randomUUID()+"_aaa.jpg",
                        UUID.randomUUID()+"_bbb.jpg",
                        UUID.randomUUID()+"_bbb.jpg"
                ));

        Long bno = boardService.register(boardDTO); //게시글 번호 반환

        log.info("bno: " + bno);
    }

    @Test
    public void testReadAll(){

        Long bno = 101L;

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        for (String fileName : boardDTO.getFileNames()){
            log.info(fileName);
        }//end for
    }


    @Test
    public void testRemoveAll(){

        Long bno =1L;

        boardService.remove(bno);
    }


    @Test
    public void testListWithAll(){

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAll(pageRequestDTO);

        List<BoardListAllDTO> dtoList = responseDTO.getDtoList();

        dtoList.forEach(boardListAllDTO -> {
            log.info(boardListAllDTO.getBno()+":"+boardListAllDTO.getTitle());

            if(boardListAllDTO.getBoardImages() != null) {
                for (BoardImageDTO boardImage : boardListAllDTO.getBoardImages()) {
                    log.info(boardImage);
                }
            }

            log.info("-------------------------------");
        });
    }
}
