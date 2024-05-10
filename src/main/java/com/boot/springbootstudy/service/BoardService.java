package com.boot.springbootstudy.service;

import com.boot.springbootstudy.dto.BoardDTO;

public interface BoardService {

    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long bno);

}
