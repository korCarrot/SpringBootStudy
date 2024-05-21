package com.boot.springbootstudy.repository.search;

import com.boot.springbootstudy.domain.Board;
import com.boot.springbootstudy.dto.BoardListReplyCountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

//목록 처리
public interface BoardSearch {
    Page<Board> search1(Pageable pageable);

    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);

    Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable);

    Page<BoardListReplyCountDTO> searchWithAll(String[] types,
                                        String keyword,
                                        Pageable pageable);

}
