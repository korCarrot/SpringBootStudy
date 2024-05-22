package com.boot.springbootstudy.service;

import com.boot.springbootstudy.domain.Board;
import com.boot.springbootstudy.dto.*;
import com.boot.springbootstudy.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService{

    private final ModelMapper modelMapper;

    private final BoardRepository boardRepository;

//    @Override
//    public Long register(BoardDTO boardDTO) {
//
//        Board board = modelMapper.map(boardDTO, Board.class);
//
//        Long bno = boardRepository.save(board).getBno();
//
//        return bno;
//    }

    @Override
    public Long register(BoardDTO boardDTO) {

        Board board = dtoToEntity(boardDTO);

        Long bno = boardRepository.save(board).getBno();

        return bno;
    }

//    @Override
//    public BoardDTO readOne(Long bno) {
//
//        Optional<Board> result = boardRepository.findById(bno);
//
//        Board board = result.orElseThrow();
//
//        BoardDTO  boardDTO = modelMapper.map(board, BoardDTO.class);
//
//        return boardDTO;
//    }

    //조회
    @Override
    public BoardDTO readOne(Long bno) {

        //board_image까지 조인 처리되는 (@EntityGraph를 이용하는) findByIdWithImages()를 사용
        Optional<Board> result = boardRepository.findByIdWithImages(bno);

        Board board = result.orElseThrow();

        BoardDTO boardDTO = entityToDTO(board);

        return boardDTO;
    }


//  수정
    @Override
    public void modify(BoardDTO boardDTO) {

        Optional<Board> result = boardRepository.findById(boardDTO.getBno());

        Board board = result.orElseThrow();

        board.change(boardDTO.getTitle(), boardDTO.getContent());

        //첨부파일의 처리
        board.clearImages();

        if(boardDTO.getFileNames() != null){
            for (String fileName : boardDTO.getFileNames()) {
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]);
            }
        }

        boardRepository.save(board);
    }


    //삭제
    @Override
    public void remove(Long bno) {

        boardRepository.deleteById(bno);

    }

    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes(); //문자열을 split으로 구분하고 배열로
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

//        log.info("keyword: " + keyword);

        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        List<BoardDTO> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board,BoardDTO.class)).collect(Collectors.toList());

        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes(); //사용자 정의 메소드
        String keyword = pageRequestDTO.getKeyword(); //Getter
        Pageable pageable = pageRequestDTO.getPageable("bno"); //사용자 정의 메소드

        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);

        //빌더 객체의 제네릭 타입을 BoardListReplyCountDTO로 지정
        return PageResponseDTO.<BoardListReplyCountDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements()) //전체 데이터 수를 반환합니다. result.getTotalElements() 메소드는 long 타입을 반환
                .build();
    }


    //게시글의 이미지와 댓글의 숫자까지 처리
    @Override
    public PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<BoardListAllDTO> result = boardRepository.searchWithAll(types, keyword, pageable);

        return PageResponseDTO.<BoardListAllDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }
}
