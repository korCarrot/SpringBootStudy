package com.boot.springbootstudy.service;

import com.boot.springbootstudy.domain.Board;
import com.boot.springbootstudy.dto.*;

import java.util.List;
import java.util.stream.Collectors;

public interface BoardService {

    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long bno);

    void modify(BoardDTO boardDTO);

    void remove(Long bno);

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    //댓글의 숫자까지 화면 구현
    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);

    //게시글의 이미지와 댓글의 숫자까지 처리
    PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);

    //DTO를 엔티티로 변환
    default Board dtoToEntity(BoardDTO boardDTO){

        Board board = Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(boardDTO.getWriter())

                .build();

        if (boardDTO.getFileNames() != null) {
            boardDTO.getFileNames().forEach(fileName -> {
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]);
            });
        }
        return board;
    }

    //엔티티를 DTO로 변환
    default BoardDTO entityToDTO(Board board) {

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();

        List<String> fileNames =
                board.getImageSet().stream().sorted().map(boardImage ->
                        boardImage.getUuid()+"_"+boardImage.getFileName()).collect(Collectors.toList());

        boardDTO.setFileNames(fileNames);

        return boardDTO;
    }

}

//default 메서드의 특징:
//기본 구현: default 메서드는 인터페이스에서 메서드의 기본 구현을 제공합니다.
//구현 클래스 재정의 가능: 구현 클래스에서는 이러한 기본 구현을 재정의(override)할 수 있습니다.
//인터페이스 확장: 인터페이스에 새로운 메서드를 추가할 때 기존의 구현 클래스들이 모두 새 메서드를 구현할 필요가 없습니다. 대신, 새 메서드를 default 메서드로 제공하여 이전 버전과의 호환성을 유지할 수 있습니다.

//주의사항:
//default 메서드는 abstract 키워드를 사용할 수 없습니다. 이는 기본 구현을 제공하기 때문에 추상 메서드가 아닙니다.
//인터페이스의 default 메서드는 객체를 생성할 수 없습니다. 따라서 인터페이스를 구현한 구현 클래스에서만 메서드를 호출할 수 있습니다.
//만약 두 개 이상의 인터페이스에서 동일한 default 메서드를 제공하는 경우, 구현 클래스에서 해당 메서드를 재정의해야 합니다.