package com.boot.springbootstudy.service;

import com.boot.springbootstudy.dto.BoardDTO;
import com.boot.springbootstudy.dto.PageRequestDTO;
import com.boot.springbootstudy.dto.PageResponseDTO;

public interface BoardService {

    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long bno);

    void modify(BoardDTO boardDTO);

    void remove(Long bno);

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);
}
